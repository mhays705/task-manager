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
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
     * @param userMapper the mapper to convert between User entities and UserDTOs
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
     * @param username the username of the user to be deleted
     * @return a ResponseEntity with status 204 (No Content) if the deletion was successful,
     *         or status 404 (Not Found) if the user was not found
     */
    @DeleteMapping("/admin/delete-user/{username}")
    public ResponseEntity<Void> deleteUser(@PathVariable String username) {

        User user = userService.getUserByUsername(username);
        if (userService.deleteUser(user.getId())) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();

    }

    /**
     *
     */
    @PostMapping("/admin/create-user")
    public ResponseEntity<UserDTO> createNewUser(@RequestBody WebUserDTO webUserDTO) {
        UserDTO newUser = userService.registerUser(webUserDTO);

        if (newUser == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }


    /**
     * Creates a new task for a specified user.
     *
     * @param id The ID of the user for whom the task is being created.
     * @param webTaskDTO The data transfer object containing the details of the task to be created.
     * @param bindingResult Holds the result of a validation and binding and contains errors that may have occurred.
     * @return ResponseEntity containing the created TaskDTO if successful, or a bad request status if there were validation errors.
     */
    @PostMapping("/admin/create-task/{id}")
    public ResponseEntity<TaskDTO> createTask(@PathVariable int id, @Valid @ModelAttribute WebTaskDTO webTaskDTO, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        User user = userService.getUserById(id)
                .map(userMapper::toEntity)
                .orElseThrow(() -> new RuntimeException("User with id: "+ id + " not found"));
        String username = user.getUsername();
        TaskDTO newTask = taskService.createTask(webTaskDTO, username);

        return ResponseEntity.status(HttpStatus.CREATED).body(newTask);
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
