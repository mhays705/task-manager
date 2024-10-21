package com.example.taskmanager.controller;

import com.example.taskmanager.dto.TaskDTO;
import com.example.taskmanager.dto.UserDTO;
import com.example.taskmanager.dto.WebTaskDTO;
import com.example.taskmanager.dto.WebUserDTO;
import com.example.taskmanager.entity.User;
import com.example.taskmanager.mapper.UserMapper;
import com.example.taskmanager.service.TaskService;
import com.example.taskmanager.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.URI;
import java.util.List;

/**
 * Rest controller to manage administrative operations for users and tasks.
 */
@RestController
@RequestMapping("/api")
public class AdminController {

	private final UserService userService;
	private final TaskService taskService;
	private final UserMapper userMapper;

	/**
	 * Constructor for AdminController that initializes the required services and mappers.
	 *
	 * @param userService the service to handle user-related operations
	 * @param taskService the service to handle task-related operations
	 * @param userMapper  the mapper to convert between User entities and UserDTOs
	 */
	@Autowired
	public AdminController(UserService userService, TaskService taskService,
						   UserMapper userMapper) {
		this.userService = userService;
		this.taskService = taskService;
		this.userMapper = userMapper;
	}


	/**
	 * Deletes a user based on the provided username.
	 *
	 * This method handles the deletion of a user from the system. It verifies if the user exists,
	 * attempts to delete the user, and adds appropriate flash attributes indicating the result
	 * of the operation.
	 *
	 * @param username the username of the user to be deleted.
	 * @param redirectAttributes attributes for a redirect scenario.
	 * @return ResponseEntity<Void> indicating the status of the operation and redirection.
	 */
	@DeleteMapping("/admin/delete-user/{username}")
	public ResponseEntity<Void> deleteUser(
			@PathVariable String username,
			RedirectAttributes redirectAttributes) {

		User user = userService.getUserByUsername(username);

		if (user == null) {
			redirectAttributes.addFlashAttribute("message", "User not found.");
			return ResponseEntity.status(HttpStatus.FOUND)
					.location(URI.create("/admin-dashboard"))
					.build();
		}

		if (userService.deleteUser(user.getId())) {
			redirectAttributes.addFlashAttribute("message", "User deleted successfully.");
		} else {
			redirectAttributes.addFlashAttribute("message", "Failed to delete user.");
		}

		return ResponseEntity.status(HttpStatus.FOUND)
				.location(URI.create("/admin-dashboard"))
				.build();
	}


	/**
	 * Creates a new user in the system based on the provided WebUserDTO details.
	 *
	 * @param webUserDTO The data transfer object containing the details of the user to be created.
	 * @return ResponseEntity containing the created UserDTO if successful, or a bad request status if the user could not be created.
	 */
	@PostMapping("/admin/create-user")
	public ResponseEntity<UserDTO> createNewUser(@RequestBody WebUserDTO webUserDTO) {
		UserDTO newUser = userService.registerUser(webUserDTO);

		if (newUser == null) {
			return ResponseEntity.badRequest().build();
		}

		return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
	}



	@PostMapping("/admin/create-task/{username}")
	public ResponseEntity<TaskDTO> createTask(@PathVariable String username, @Valid @ModelAttribute WebTaskDTO webTaskDTO, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}

		try {
			TaskDTO newTask = taskService.createTask(webTaskDTO, username);
			return ResponseEntity.status(HttpStatus.CREATED).body(newTask);
		}

		catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}


	/**
	 * Deletes a list of user tasks based on the provided task IDs.
	 *
	 * @param selectedItems - List of task IDs to be deleted.
	 * @return ResponseEntity<String> - Returns a response indicating the outcome of the delete operation.
	 */
	@DeleteMapping("/admin/delete-user-tasks")
	public ResponseEntity<String> deleteUserTasks(@RequestParam List<Integer> selectedItems) {

		if (selectedItems == null || selectedItems.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No tasks selected to delete");
		}

		for (Integer id : selectedItems) {
			taskService.deleteTask(id);
		}

		return ResponseEntity.ok("Delete of tasks has been completed.");


	}


}
