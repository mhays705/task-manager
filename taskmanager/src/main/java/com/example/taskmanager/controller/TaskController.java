package com.example.taskmanager.controller;

import com.example.taskmanager.dto.TaskDTO;
import com.example.taskmanager.dto.WebTaskDTO;
import com.example.taskmanager.entity.User;
import com.example.taskmanager.service.TaskService;
import com.example.taskmanager.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TaskController {

    private final TaskService taskService;
    private final UserService userService;

    @Autowired
    public TaskController(UserService userService, TaskService taskService) {
        this.taskService = taskService;
        this.userService = userService;
    }





    @PostMapping("/task/create-task")
    public ResponseEntity<TaskDTO> createTask(@RequestBody WebTaskDTO webTaskDTO, HttpSession session) {
        User user = userService.getCurrentUser(session);

        if (user == null) {
            return ResponseEntity.badRequest().build();
        }

        TaskDTO taskDTO = taskService.createTask(webTaskDTO, user);

        return ResponseEntity.status(HttpStatus.CREATED).body(taskDTO);
    }








}
