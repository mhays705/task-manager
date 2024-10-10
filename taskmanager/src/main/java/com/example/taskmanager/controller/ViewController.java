package com.example.taskmanager.controller;

import com.example.taskmanager.dto.WebTaskDTO;
import com.example.taskmanager.dto.WebUserDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {


	@GetMapping("/")
	public String showLandingPage() {
		return "landing";
	}


	@GetMapping("/access-denied")
	public String showAccessDenied() { return "access-denied"; }

	@GetMapping("/login")
	public String showLoginForm() {
		return "login-form";
	}

	@GetMapping("/register-user")
	public String registerUser(Model model){
		model.addAttribute("webUser", new WebUserDTO());
		return "register-user";
	}

	@GetMapping("/dashboard")
	public String showUserDashboard() {
		return "user-dashboard";
	}

	@GetMapping("/create-task")
	public String createTask(Model model) {
		model.addAttribute("webTaskDTO", new WebTaskDTO());
		return "create-task";
	}


}
