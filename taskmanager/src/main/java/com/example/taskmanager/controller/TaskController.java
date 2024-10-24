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
	 * Handles the POST request to create a new task.
	 *
	 * This method is responsible for processing the form submission for creating a new task.
	 * It validates the input data, creates the task, and redirects to the dashboard with a success message upon successful creation.
	 *
	 * @param webTaskDTO the data transfer object containing task details
	 * @param bindingResult the result of the validation and data binding
	 * @param authentication the authentication object containing the current user details
	 * @param redirectAttributes attributes for the redirect scenario, used to store flash attributes
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
		taskService.createTask(webTaskDTO, username);

		redirectAttributes.addFlashAttribute("successMessage", "Task created successfully!");
		return "redirect:/dashboard";
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
	@DeleteMapping("/delete")
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
