package com.example.taskmanager.mapper;

import com.example.taskmanager.dto.TaskDTO;
import com.example.taskmanager.entity.Task;
import com.example.taskmanager.entity.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * TaskMapperImpl is an implementation of the TaskMapper interface,
 * responsible for converting between Task entities and TaskDTOs.
 */
@Component
public class TaskMapperImpl implements TaskMapper {

	/**
	 * Converts a Task entity to a TaskDTO.
	 *
	 * @param task The Task entity to be converted.
	 * @return The converted TaskDTO.
	 */
	@Override
	public TaskDTO toDTO(Task task) {

		TaskDTO taskDTO = TaskDTO.builder()
				.id(task.getId())
				.taskName(task.getTaskName())
				.startDate(task.getStartDate())
				.dueDate(task.getDueDate())
				.taskStatus(task.isTaskStatus())
				.build();

		return taskDTO;
	}


	/**
	 * Converts an iterable collection of Task entities to a list of TaskDTOs.
	 *
	 * @param tasks an iterable collection of Task entities that need to be converted.
	 * @return a list of TaskDTO objects converted from the provided Task entities.
	 */
	@Override
	public List<TaskDTO> toDTO(Iterable<Task> tasks) {
		List<TaskDTO> taskDTOS = new ArrayList<>();

		for (Task task : tasks) {
			taskDTOS.add(toDTO(task));
		}

		return taskDTOS;
	}

	/**
	 * Converts a TaskDTO object to a Task entity.
	 *
	 * @param taskDTO the TaskDTO object to be converted
	 * @param user the User object to be associated with the Task entity
	 * @return the converted Task entity, or null if the input TaskDTO is null
	 */
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
				.taskStatus(taskDTO.isTaskStatus())
				.build();


	}


	/**
	 * Converts an iterable of TaskDTO objects to a list of Task entities,
	 * associating each Task with the specified User.
	 *
	 * @param taskDTOS Iterable of TaskDTO objects to be converted.
	 * @param user User entity to be associated with each Task.
	 * @return List of Task entities converted from the provided TaskDTO objects.
	 */
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




