package com.example.taskmanager.controller;

import com.example.taskmanager.dto.TaskDTO;
import com.example.taskmanager.dto.WebTaskDTO;
import com.example.taskmanager.dto.WebUserDTO;
import com.example.taskmanager.entity.User;
import com.example.taskmanager.service.TaskService;
import com.example.taskmanager.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * The ViewController class manages the HTTP request-response workflow for
 * various pages of the application including user registration, login, task management,
 * and admin dashboard.
 */
@Controller
public class ViewController {

	private final TaskService taskService;
	private final UserService userService;

	/**
	 * Constructs a new {@code ViewController} with the specified services.
	 *
	 * @param taskService the service responsible for handling task-related operations
	 * @param userService the service responsible for handling user-related operations
	 */
	public ViewController(TaskService taskService, UserService userService) {
		this.taskService = taskService;
		this.userService = userService;
	}

	/**
	 * Handles HTTP GET requests to the root URL and returns the view name
	 * for the landing page.
	 *
	 * @return the view name for the landing page, which is "landing"
	 */
	@GetMapping("/")
	public String showLandingPage() {
		return "landing";
	}


	/**
	 * Handles the request for the access denied page.
	 *
	 * @return A string indicating the view name for the access denied page.
	 */
	@GetMapping("/access-denied")
	public String showAccessDenied() {
		return "access-denied";
	}

	/**
	 * Handles GET requests to the "/login" endpoint.
	 *
	 * @return The name of the view to be rendered, which is "login-form".
	 */
	@GetMapping("/login")
	public String showLoginForm() {
		return "login-form";
	}

	/**
	 * Displays the user registration form.*
	 * This method handles GET requests to "/register-new-user". It adds an empty WebUserDTO
	 * object to the model to be used by the registration form.
	 *
	 * @param model The Model object used to pass attributes to the view.
	 * @return The name of the view template for user registration.
	 */
	@GetMapping("/register-new-user")
	public String showUserRegistrationForm(Model model) {

		model.addAttribute("webUserDTO", new WebUserDTO());

		return "user-registration-form";

	}

	/**
	 * Displays the user dashboard page if the user is authenticated. Redirects to the login page if the user is not authenticated.
	 * If the user has an admin role, redirects to the admin dashboard instead.
	 *
	 * @param model the model to which task data is added
	 * @param authentication the authentication object containing user details and roles
	 * @return the view name of the user dashboard or redirection to the appropriate page based on the user's role and authentication status
	 */
	@GetMapping("/dashboard")
	public String showUserDashboard(Model model, Authentication authentication) {
		if (authentication == null || !authentication.isAuthenticated()) {
			return "redirect:/login";
		}

		UserDetails userDetails = (UserDetails) authentication.getPrincipal();

		if (userDetails.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"))) {
			return "redirect:/admin/dashboard";
		}

		int id = userService.getUserByUsername(userDetails.getUsername()).getId();

		List<TaskDTO> tasks = taskService.getTasksByUserId(id);

		model.addAttribute("tasks", tasks);

		return "user-dashboard";
	}

	/**
	 * Displays the page for creating a new task.
	 *
	 * @param model the Model object used to pass attributes from the controller to the view
	 * @return the name of the view for creating a task
	 */
	@GetMapping("/create-task")
	public String showCreateTaskPage(Model model) {
		model.addAttribute("webTaskDTO", new WebTaskDTO());
		return "create-task";
	}

	/**
	 * Handles requests to show the delete tasks page for the authenticated user.
	 *
	 * @param model          The model to which the list of tasks will be added.
	 * @param authentication Provides the authentication object containing details about the user.
	 * @return A string representing the name of the view to display.
	 */
	@GetMapping("/delete-tasks")
	private String showDeleteTasksPage(Model model, Authentication authentication) {

		if (authentication == null || !authentication.isAuthenticated()) {
			return "redirect:/login";
		}
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();

		String username = userDetails.getUsername();
		User user = userService.getUserByUsername(username);
		int userId = user.getId();
		List<TaskDTO> tasks = taskService.getTasksByUserId(userId);
		model.addAttribute("tasks", tasks);
		return "delete-tasks";
	}

	/**
	 * Displays the page to update task statuses for the authenticated user.
	 *
	 * This method fetches the tasks for the currently authenticated user and adds them to the model.
	 * If the user is not authenticated, it redirects to the login page.
	 *
	 * @param model the model object to add attributes to, for rendering the view
	 * @param authentication the authentication object containing the user's authentication details
	 * @return the name of the view to render, or a redirection to the login page if the user is not authenticated
	 */
	@GetMapping("/update-task-status")
	public String showUpdateTaskStatusPage(Model model, Authentication authentication) {

		if (authentication == null || !authentication.isAuthenticated()) {
			return "redirect:/login";
		}
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		String username = userDetails.getUsername();
		User user = userService.getUserByUsername(username);
		List<TaskDTO> tasks = taskService.getTasksByUserId(user.getId());
		model.addAttribute("tasks", tasks);

		for (TaskDTO task : tasks) {
			System.out.println(task.toString());
		}

		return "update-task-status";
	}


	/**
	 * Handles the GET request to display the update user information page.
	 *
	 * This method retrieves the authenticated user's details and adds them to the model
	 * as a WebUserDTO object, which is then used in the view to populate the user update form.
	 *
	 * @param model the Spring Model object used to pass attributes to the view
	 * @param authentication the current user's authentication object which contains the user details
	 * @return the name of the view to be rendered, in this case "update-user-info"
	 */
	@GetMapping("/update-user-info")
	public String showUpdateUserInfo(Model model, Authentication authentication) {

		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		User user = userService.getUserByUsername(userDetails.getUsername());
		WebUserDTO webUserDTO = WebUserDTO.builder()
				.username(user.getUsername())
				.firstName(user.getFirstName())
				.lastName(user.getLastName())
				.email(user.getEmail())
				.build();
		model.addAttribute("webUserDTO", webUserDTO);

		return "update-user-info";
	}

	@GetMapping("/admin-create-user-task/{username}")
	public String showCreateUserTask(@PathVariable String username, Model model) {

		model.addAttribute("webTaskDTO", new WebTaskDTO());
		model.addAttribute("username", username);

		return "admin-create-user-task";
	}


}
