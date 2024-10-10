package com.example.taskmanager.controller;

import com.example.taskmanager.dto.WebTaskDTO;
import com.example.taskmanager.mapper.UserMapper;
import com.example.taskmanager.service.TaskService;
import com.example.taskmanager.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

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








}
