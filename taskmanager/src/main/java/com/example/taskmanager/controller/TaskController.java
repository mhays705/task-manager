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

@RestController
@RequestMapping("/api")
public class TaskController {

    private final TaskService taskService;
    private final UserService userService;
	private final UserMapper userMapper;

    @Autowired
    public TaskController(UserService userService, TaskService taskService,
						  UserMapper userMapper) {
        this.taskService = taskService;
        this.userService = userService;
		this.userMapper = userMapper;
    }





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

	@DeleteMapping("/tasks/delete-tasks")
	public ResponseEntity<String> deleteTask(@RequestParam List<Integer> selectedItems) {

		if(selectedItems == null || selectedItems.isEmpty()) {
			return ResponseEntity.badRequest().body("No tasks selected for deletion");
		}

		for (Integer id : selectedItems) {
			taskService.deleteTask(id);
		}

		return ResponseEntity.status(HttpStatus.FOUND).header(HttpHeaders.LOCATION, "/dashboard").build();
	}








}
