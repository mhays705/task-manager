package com.example.taskmanager.service;

import com.example.taskmanager.dto.TaskDTO;
import com.example.taskmanager.dto.WebTaskDTO;
import com.example.taskmanager.entity.Task;
import com.example.taskmanager.entity.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TaskService {

    Optional<TaskDTO> getTaskById(int id);

    List<TaskDTO> getAllTasks();

    TaskDTO createTask(WebTaskDTO webTaskDTO, String username);

    void deleteTask(int id);

    Optional<TaskDTO> getTaskByStartDate(LocalDate startDate);

    Optional<TaskDTO> getTaskByDueDate(LocalDate dueDate);

    boolean toggleTaskStatus(int id);


}
