package com.example.taskmanager.mapper;

import com.example.taskmanager.dto.TaskDTO;
import com.example.taskmanager.entity.Task;
import com.example.taskmanager.entity.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TaskMapperImpl implements TaskMapper {

	@Override
	public TaskDTO toDTO(Task task) {

		TaskDTO taskDTO = TaskDTO.builder()
				.id(task.getId())
				.taskName(task.getTaskName())
				.startDate(task.getStartDate())
				.dueDate(task.getDueDate())
				.build();

		return taskDTO;
	}


	@Override
	public List<TaskDTO> toDTO(Iterable<Task> tasks) {
		List<TaskDTO> taskDTOS = new ArrayList<>();

		for (Task task : tasks) {
			taskDTOS.add(toDTO(task));
		}

		return taskDTOS;
	}

	@Override
	public Task toEntity(TaskDTO taskDTO, User user) {

		if (taskDTO == null) {
			return null;
		}

        return Task.builder()
				.id(taskDTO.getId())
				.taskName(taskDTO.getTaskName())
				.startDate(taskDTO.getStartDate())
				.dueDate(taskDTO.getDueDate())
				.user(user)
				.build();


	}


	@Override
	public List<Task> toEntity(Iterable<TaskDTO> taskDTOS, User user) {
		List<Task> tasks = new ArrayList<>();

		for (TaskDTO taskDTO : taskDTOS) {
			if (taskDTO != null) { // Check for null TaskDTO
				tasks.add(toEntity(taskDTO, user)); // Convert and add to the list
			}
		}

		return tasks;
	}


}




