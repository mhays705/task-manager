package com.example.taskmanager.controller;


import com.example.taskmanager.dto.WebTaskDTO;
import com.example.taskmanager.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/task")
public class TaskController {

	private final TaskService taskService;

	/**
	 * Constructor for TaskController.
	 *
	 * This constructor injects the TaskService dependency, which is used for managing
	 * task-related operations like creating, deleting, and updating tasks.
	 *
	 * @param taskService the service used for task operations
	 */
	@Autowired
	public TaskController(TaskService taskService) {
		this.taskService = taskService;
	}


	/**
	 * Handles the creation of a new task.
	 *
	 * This method processes the incoming request to create a new task by validating the provided
	 * data, and delegates the task creation to the task service. It also handles any validation
	 * errors and displays appropriate flash messages based on the outcome of the operation.
	 *
	 * @param webTaskDTO the Data Transfer Object containing task details to be created
	 * @param bindingResult the object holding the result of the validation and binding
	 * @param authentication the authentication object containing the currently authenticated user's details
	 * @param redirectAttributes attributes for storing flash attributes to be used in a redirect scenario
	 * @return the view name to be redirected to after processing the request
	 */
	@PostMapping("/create")
	public String createTask(@Valid @ModelAttribute("webTaskDTO")WebTaskDTO webTaskDTO,
							 BindingResult bindingResult,
							 Authentication authentication,
							 RedirectAttributes redirectAttributes) {

		if (bindingResult.hasErrors()) {
			return "create-task";
		}

		String username = authentication.getName();
		try {
			taskService.createTask(webTaskDTO, username);
			redirectAttributes.addFlashAttribute("successMessage", "Task created successfully!");
			return "redirect:/dashboard";
		}
		catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "Task Creation Failed" + e.getMessage());
			return "redirect:/dashboard";
		}
	}


	/**
	 * Handles the deletion of selected tasks.
	 *
	 * This method processes the incoming request to delete multiple tasks specified by their IDs.
	 * It delegates the actual deletion to the task service and stores a success message
	 * as a flash attribute for redirection to the dashboard.
	 *
	 * @param selectedItems a list of task IDs to be deleted
	 * @param redirectAttributes attributes for storing flash attributes to be used in a redirect scenario
	 * @return the view name to be redirected to after processing the request
	 */
	@DeleteMapping("/delete-tasks")
	public String deleteTasks(@RequestParam List<Integer> selectedItems,
							  RedirectAttributes redirectAttributes) {

		for (int id : selectedItems) {
			taskService.deleteTask(id);
		}

		redirectAttributes.addFlashAttribute("successMessage", "Selected tasks successfully delete.");

		return "redirect:/dashboard";
	}

	/**
	 * Updates the status of selected tasks.
	 *
	 * This method toggles the status of each task specified by its ID, provided in the selectedItems list.
	 * It delegates the status toggling to the task service and stores a success message
	 * as a flash attribute for redirection to the dashboard.
	 *
	 * @param selectedItems a list of task IDs whose status needs to be updated
	 * @param redirectAttributes attributes for storing flash attributes to be used in a redirect scenario
	 * @return the view name to be redirected to after processing the request
	 */
	@PatchMapping("/update-status")
	public String updateTaskStatus(@RequestParam List<Integer> selectedItems,
								   RedirectAttributes redirectAttributes) {

		for (int id : selectedItems) {
			taskService.toggleTaskStatus(id);
		}

		redirectAttributes.addFlashAttribute("successMessage", "Selected tasks status' updated.");

		return "redirect:/dashboard";
	}


}
