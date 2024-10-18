package com.example.taskmanager.controller;

import com.example.taskmanager.dto.WebTaskDTO;
import com.example.taskmanager.mapper.UserMapper;
import com.example.taskmanager.service.TaskService;
import com.example.taskmanager.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/**
 * TaskController is a REST controller for handling task-related operations.
 *
 * This controller provides endpoints for creating, deleting, and updating the status of tasks.
 * It interacts with the TaskService to perform these operations and returns appropriate
 * HTTP status codes and response bodies.
 */
@RestController
@RequestMapping("/api")
public class TaskController {

    private final TaskService taskService;
    private final UserService userService;
	private final UserMapper userMapper;

    /**
	 * Constructs a new TaskController with the specified userService, taskService, and userMapper.
	 *
	 * @param userService the user service to interact with user-related operations
	 * @param taskService the task service to manage task-related operations
	 * @param userMapper  the user mapper for converting User entities to DTOs
	 */
	@Autowired
    public TaskController(UserService userService, TaskService taskService,
						  UserMapper userMapper) {
        this.taskService = taskService;
        this.userService = userService;
		this.userMapper = userMapper;
    }





    /**
	 * Creates a new task based on the provided WebTaskDTO and current authenticated user.
	 *
	 * @param webTaskDTO the Data Transfer Object containing task details for task creation.
	 * @param bindingResult encapsulates validation results after binding the provided WebTaskDTO.
	 * @return a ResponseEntity indicating the result of the task creation;
	 *         returns a 302 FOUND status on successful creation with a location header to the dashboard,
	 *         and a 400 BAD REQUEST status if there are validation errors or the user is not authenticated.
	 */
	@PostMapping("/task/create-task")
    public ResponseEntity<String> createTask(@Valid @ModelAttribute WebTaskDTO webTaskDTO,
                                             BindingResult bindingResult) {

        if (bindingResult.hasErrors()){
            return ResponseEntity.badRequest().build();
        }
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated()) {
			return ResponseEntity.badRequest().build();
		}

		UserDetails userDetails = (UserDetails) authentication.getPrincipal();

		String username = userDetails.getUsername();
        taskService.createTask(webTaskDTO, username);

        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create("/dashboard")).build();
    }

	/**
	 * Deletes the selected tasks.
	 *
	 * This method removes the tasks identified by the provided list of IDs from the system.
	 * If no tasks are selected, it returns a bad request response.
	 * On successful deletion of tasks, it redirects to the dashboard.
	 *
	 * @param selectedItems A list of task IDs to be deleted. Must not be null or empty.
	 * @return A ResponseEntity with an appropriate HTTP status and response body.
	 */
	@DeleteMapping("/task/delete-tasks")
	public ResponseEntity<String> deleteTask(@RequestParam List<Integer> selectedItems) {

		if(selectedItems == null || selectedItems.isEmpty()) {
			return ResponseEntity.badRequest().body("No tasks selected for deletion");
		}

		for (Integer id : selectedItems) {
			taskService.deleteTask(id);
		}

		return ResponseEntity.status(HttpStatus.FOUND).header(HttpHeaders.LOCATION, "/dashboard").build();
	}

	/**
	 * Updates the status of the selected tasks.
	 *
	 * This method receives a list of task IDs and toggles their status
	 * between completed and not completed. If the list is null or empty,
	 * it returns a bad request response.
	 *
	 * @param selectedItems the list of task IDs to update
	 * @return a ResponseEntity indicating the result of the operation,
	 * including a redirect to the dashboard on success or an error message on failure
	 */
	@PatchMapping("/task/update-status")
	public ResponseEntity<String> updateTaskStatus(@RequestParam List<Integer> selectedItems) {

		if (selectedItems == null || selectedItems.isEmpty()) {
			return ResponseEntity.badRequest().body("No tasks selected to mark complete.");
		}

		for (Integer id : selectedItems) {
			taskService.toggleTaskStatus(id);
		}

		return ResponseEntity.status(HttpStatus.FOUND).header(HttpHeaders.LOCATION,"/dashboard").build();


	}








}
