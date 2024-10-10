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
	public String registerUser(Model model) {
		model.addAttribute("webUser", new WebUserDTO());
		return "register-user";
	}

	@GetMapping("/dashboard")
	public String showUserDashboard(Model model, Authentication authentication) {
		if (authentication == null || !authentication.isAuthenticated()) {
			return "redirect:/login";
		}

		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		int id = userService.getUserByUsername(userDetails.getUsername()).getId();

		List<TaskDTO> tasks = taskService.getTasksByUserId(id);

		model.addAttribute("tasks", tasks);

		return "user-dashboard";
	}

	@GetMapping("/create-task")
	public String createTask(Model model) {
		model.addAttribute("webTaskDTO", new WebTaskDTO());
		return "create-task";
	}


}
