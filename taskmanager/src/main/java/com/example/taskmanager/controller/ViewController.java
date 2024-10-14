package com.example.taskmanager.controller;

import com.example.taskmanager.dto.TaskDTO;
import com.example.taskmanager.dto.UserDTO;
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

@Controller
public class ViewController {

	private final TaskService taskService;
	private final UserService userService;

	public ViewController(TaskService taskService, UserService userService) {
		this.taskService = taskService;
		this.userService = userService;
	}

	@GetMapping("/")
	public String showLandingPage() {
		return "landing";
	}


	@GetMapping("/access-denied")
	public String showAccessDenied() {
		return "access-denied";
	}

	@GetMapping("/login")
	public String showLoginForm() {
		return "login-form";
	}

	@GetMapping("/register-user")
	public String showRegisterUserPage(Model model) {
		model.addAttribute("webUser", new WebUserDTO());
		return "register-user";
	}

	@GetMapping("/dashboard")
	public String showUserDashboard(Model model, Authentication authentication) {
		if (authentication == null || !authentication.isAuthenticated()) {
			return "redirect:/login";
		}

		UserDetails userDetails = (UserDetails) authentication.getPrincipal();

		if (userDetails.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"))) {
			return "redirect:/admin-dashboard";
		}

		int id = userService.getUserByUsername(userDetails.getUsername()).getId();

		List<TaskDTO> tasks = taskService.getTasksByUserId(id);

		model.addAttribute("tasks", tasks);

		return "user-dashboard";
	}

	@GetMapping("/create-task")
	public String showCreateTaskPage(Model model) {
		model.addAttribute("webTaskDTO", new WebTaskDTO());
		return "create-task";
	}

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

	@GetMapping("/update-task-status")
	public String showCompleteTasksPage(Model model, Authentication authentication) {

		if (authentication == null || !authentication.isAuthenticated()) {
			return "redirect:/login";
		}
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		String username = userDetails.getUsername();
		User user = userService.getUserByUsername(username);
		List<TaskDTO> tasks = taskService.getTasksByUserId(user.getId());
		model.addAttribute("tasks", tasks);

		for(TaskDTO task : tasks) {
			System.out.println(task.toString());
		}

		return "update-task-status";
	}


	@GetMapping("/register-new-user")
	public	String showUserRegistrationForm(Model model) {

		model.addAttribute("webUserDTO", new WebUserDTO());

		return "user-registration-form";

	}

	@GetMapping("/admin-dashboard")
	public String showAdminDashboard(Model model, Authentication authentication) {

		if (authentication == null || !authentication.isAuthenticated()) {
			return "redirect:/login";
		}

		List<UserDTO> users = userService.getAllUsers();

		model.addAttribute("users", users);

		return "admin-dashboard";

	}


	@GetMapping("/admin-user-tasks/{username}")
	public String deleteUserTasks(@PathVariable String username, Model model) {

		User user = userService.getUserByUsername(username);
		List<TaskDTO> tasks = taskService.getTasksByUserId(user.getId());
		model.addAttribute("tasks", tasks);

		return "admin-user-tasks";



	}


}
