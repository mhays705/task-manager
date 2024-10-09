package com.example.taskmanager.mapper;

import com.example.taskmanager.dto.TaskDTO;
import com.example.taskmanager.entity.Task;
import com.example.taskmanager.entity.User;

import java.util.List;

public interface TaskMapper {

	TaskDTO toDTO(Task task);

	List<TaskDTO> toDTO (Iterable<Task> tasks);


	List<Task> toEntity(Iterable<TaskDTO> taskDTOS, User user);

	Task toEntity(TaskDTO taskDTO, User user);

}
