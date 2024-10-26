package com.example.taskmanager.controller;


import com.example.taskmanager.dto.TaskDTO;
import com.example.taskmanager.dto.UserDTO;
import com.example.taskmanager.dto.WebTaskDTO;
import com.example.taskmanager.dto.WebUserDTO;
import com.example.taskmanager.entity.Role;
import com.example.taskmanager.entity.User;
import com.example.taskmanager.service.RoleService;
import com.example.taskmanager.service.TaskService;
import com.example.taskmanager.service.UserService;
import com.example.taskmanager.validation.OnCreate;
import com.example.taskmanager.validation.OnUpdate;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {


	private final UserService userService;
	private final TaskService taskService;
	private final RoleService roleService;


	/**
	 * Constructs an AdminController with the specified services.
	 *
	 * @param userService the service for user operations
	 * @param taskService the service for task operations
	 * @param roleService the service for role operations
	 */
	@Autowired
	public AdminController(UserService userService,
						   TaskService taskService,
						   RoleService roleService) {
		this.userService = userService;
		this.taskService = taskService;
		this.roleService = roleService;
	}


	/**
	 * Displays the admin dashboard.
	 *
	 * This method is responsible for rendering the administrator's dashboard view. It checks if the
	 * current user is authenticated and has the appropriate administrative roles. If not, it redirects
	 * the user to the appropriate login or user dashboard pages. Otherwise, it loads all users and
	 * includes this data in the model for the admin dashboard view.
	 *
	 * @param model the model object to add attributes to be used in the view
	 * @param authentication the authentication object representing the currently logged-in user
	 * @return a string indicating the view name to be rendered
	 */
	@GetMapping("/dashboard")
	public String showAdminDashboard(Model model, Authentication authentication) {

		if (authentication == null || !authentication.isAuthenticated()) {
			return "redirect:/login";
		}

		if (authentication.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_USER"))) {
			return "redirect:/dashboard";
		}

		List<UserDTO> users = userService.getAllUsers();
		model.addAttribute("users", users);

		return "admin-dashboard";
	}

	/**
	 * Displays the tasks associated with a specific user for an admin, allowing for their deletion.
	 *
	 * @param username the username of the user whose tasks are to be displayed
	 * @param model the Model object used to pass attributes to the view
	 * @return the name of the view to render, which in this case is "admin-user-tasks"
	 */
	@GetMapping("/user-tasks/{username}")
	public String showUserTasks(@PathVariable String username, Model model) {

		User user = userService.getUserByUsername(username);
		List<TaskDTO> tasks = taskService.getTasksByUserId(user.getId());
		model.addAttribute("tasks", tasks);
		model.addAttribute("username", username);

		return "admin-user-tasks";
	}

	/**
	 * Displays the page for creating a user-specific task.
	 *
	 * @param username the username of the user for whom the task is to be created
	 * @param model the Model object used for passing attributes to the view
	 * @return the name of the view for creating a user task, which is "admin-create-user-task"
	 */
	@GetMapping("/create-user-task/{username}")
	public String showCreateUserTask(@PathVariable String username, Model model) {

		model.addAttribute("webTaskDTO", new WebTaskDTO());
		model.addAttribute("username", username);

		return "admin-create-user-task";
	}

	/**
	 * Displays the page for creating a new user.
	 *
	 * This method handles the GET request to show the user creation form for the admin.
	 * It initializes a new instance of the WebUserDTO and adds it to the model to be used in the view.
	 *
	 * @param model the model object to add attributes to be used in the view
	 * @return a string indicating the name of the view to be rendered, which is "admin-create-new-user"
	 */
	@GetMapping("/create-new-user")
	public String createNewUser(Model model) {
		model.addAttribute("webUserDTO", new WebUserDTO());
		return "admin-create-user";
	}

	@GetMapping("/update-user-info/{username}")
	public String updateUserInfo(@PathVariable("username") String username,
								 Model model) {
		User user = userService.getUserByUsername(username);
		WebUserDTO webUserDTO = WebUserDTO.builder()
				.username(user.getUsername())
				.firstName(user.getFirstName())
				.lastName(user.getLastName())
				.email(user.getEmail())
				.roles(user.getRoles())
				.build();
		model.addAttribute("webUserDTO", webUserDTO);
		List<Role> roles = roleService.findAll();
		model.addAttribute("roles", roles);

		return "admin-update-user-info";
	}



	/**
	 * Updates the user information.
	 *
	 * This method handles the update of a user's information. It validates the provided data and updates
	 * the user if the data is valid. In case of validation errors, it returns to the user info update page.
	 * Upon successful update, it redirects to the admin dashboard with a success message or an error message if the update fails.
	 *
	 * @param webUserDTO the Data Transfer Object containing the updated user information
	 * @param bindingResult holds the result of the validation and binding of the webUserDTO
	 * @param redirectAttributes attributes for a redirect scenario to pass along flash attributes
	 * @return a string representing the view name to be rendered or the redirect target
	 */
	@PatchMapping("/update-user-info")
	public String updateUserInfo(@Validated(OnUpdate.class) @ModelAttribute("webUserDTO")WebUserDTO webUserDTO,
								 BindingResult bindingResult,
								 RedirectAttributes redirectAttributes) {
		if (bindingResult.hasErrors()) {
			return "admin-update-user-info";
		}

		try {
			userService.updateUser(webUserDTO);
			redirectAttributes.addFlashAttribute("successMessage", "User successfully updated.");
			System.out.println("should show message");
			return "redirect:/admin/dashboard";
		}

		catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "Update of user has failed.");
			return "redirect:/admin/dashboard";
		}

	}


	/**
	 * Creates a new user task based on the provided WebTaskDTO and associates it with the specified user.
	 *
	 * This method handles the POST request to create a task for a user identified by their username.
	 * It validates the input data and redirects accordingly based on the presence of validation errors.
	 * The method also handles unexpected exceptions by redirecting and setting an appropriate error message.
	 *
	 * @param username the username of the user to whom the task will be assigned
	 * @param webTaskDTO the Data Transfer Object containing the task details
	 * @param bindingResult holds the result of the validation and binding of the webTaskDTO
	 * @param redirectAttributes attributes for a redirect scenario to pass along flash attributes
	 * @return a string representing the view name to be rendered or the redirect target
	 */
	@PostMapping("/create-user-task/{username}")
	public String createUserTask(@PathVariable String username,
								 @Valid @ModelAttribute("webTaskDTO") WebTaskDTO webTaskDTO,
								 BindingResult bindingResult,
								 RedirectAttributes redirectAttributes,
								 Model model) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("username", username);
			return "admin-create-user-task";
		}
		try {
			taskService.createTask(webTaskDTO, username);
			redirectAttributes.addFlashAttribute("successMessage", "Task successfully created for " + username);
			return "redirect:/admin/dashboard";
		}
		catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "An unexpected error occured: " + e.getMessage());
			return "redirect:/admin/dashboard";
		}
	}


	/**
	 * Deletes the tasks specified by the list of task IDs.
	 *
	 * This method handles deletion of tasks for a user by accepting a list of task IDs and
	 * deleting those tasks from the system. It adds appropriate flash messages to provide
	 * feedback to the user about the success or failure of the operation.
	 *
	 * @param selectedItems the list of task IDs to be deleted
	 * @param redirectAttributes attributes to be used in the redirect scenario to pass along flash attributes
	 * @return a string representing the redirect target to the admin user tasks view
	 */
	@DeleteMapping("/delete-user-tasks")
	public String deleteUserTasks(@RequestParam List<Integer> selectedItems,
								  @PathVariable String username,
								  RedirectAttributes redirectAttributes) {
		if (selectedItems == null || selectedItems.isEmpty()) {
			redirectAttributes.addFlashAttribute("error", "No tasks selected.");
			return "redirect:/admin/user-tasks/" + username;
		}

		for (int id : selectedItems) {
			taskService.deleteTask(id);
		}

		redirectAttributes.addFlashAttribute("successMessage", "Selected items successfully delete");
		return "redirect:/admin/user-tasks/" + username;

	}

	/**
	 * Deletes a user based on the provided username.
	 *
	 * This method handles HTTP DELETE requests to delete a user identified by the given username.
	 * If the user is found, it attempts to delete the user and sets appropriate flash attributes
	 * indicating success or failure. If the user is not found, it sets an error flash attribute.
	 *
	 * @param username the username of the user to be deleted
	 * @param redirectAttributes attributes for the redirect scenario to pass along flash messages
	 * @return a string representing the redirect target to the admin dashboard view
	 */
	@DeleteMapping("/delete-user/{username}")
	public String adminDeleteUser(@PathVariable String username,
								  RedirectAttributes redirectAttributes) {
		User user = userService.getUserByUsername(username);

		if (user == null) {
			redirectAttributes.addFlashAttribute("error", "User not found.");
			return "redirect:/admin/dashboard";
		}
		int id = user.getId();
		try {
			userService.deleteUser(id);
			redirectAttributes.addFlashAttribute("successMessage", "User successfully deleted.");
			return "redirect:/admin/dashboard";
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "There was an error processing the deletion");
			return "redirect:/admin/dashboard";
		}
	}

	@PostMapping("/create-new-user")
	public String createNewUser(@Validated(OnCreate.class) @ModelAttribute("webUserDTO") WebUserDTO webUserDTO,
								BindingResult bindingResult,
								RedirectAttributes redirectAttributes) {

		if (!webUserDTO.getPassword().equals(webUserDTO.getPasswordConfirmation())){
			bindingResult.rejectValue("passwordConfirmation", null, "Passwords do not match.");
		}

		try {
			userService.registerUser(webUserDTO);
			redirectAttributes.addFlashAttribute("successMessage", "User was successfully created.");
			return "redirect:/admin/dashboard";
		}
		catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "User Creation Failed " + e.getMessage());
			return "admin-create-user";
		}
	}




}
