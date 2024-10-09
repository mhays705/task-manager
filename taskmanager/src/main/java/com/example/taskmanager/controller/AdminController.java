package com.example.taskmanager.controller;

import com.example.taskmanager.dto.TaskDTO;
import com.example.taskmanager.dto.UserDTO;
import com.example.taskmanager.dto.WebTaskDTO;
import com.example.taskmanager.dto.WebUserDTO;
import com.example.taskmanager.entity.User;
import com.example.taskmanager.mapper.UserMapper;
import com.example.taskmanager.service.TaskService;
import com.example.taskmanager.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class AdminController {

    private final UserService userService;
    private final TaskService taskService;
    private final UserMapper userMapper;

    @Autowired
    public AdminController(UserService userService, TaskService taskService,
                           UserMapper userMapper) {
        this.userService = userService;
        this.taskService = taskService;
        this.userMapper = userMapper;
    }


    @DeleteMapping("/admin/delete-user/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable int id) {
        if (userService.deleteUser(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();

    }

    @PostMapping("/admin/create-user")
    public ResponseEntity<UserDTO> createNewUser(@RequestBody WebUserDTO webUserDTO) {
        UserDTO newUser = userService.registerUser(webUserDTO);

        if (newUser == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }


    @PostMapping("/admin/create-task/{id}")
    public ResponseEntity<TaskDTO> createTask(@PathVariable int id, @RequestBody WebTaskDTO webTaskDTO) {
        Optional<UserDTO> user = userService.getUserById(id);
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        UserDTO userDTO= user.get();

        TaskDTO newTask = taskService.createTask(webTaskDTO, userMapper.toEntity(userDTO));

        return ResponseEntity.status(HttpStatus.CREATED).body(newTask);


    }


}
