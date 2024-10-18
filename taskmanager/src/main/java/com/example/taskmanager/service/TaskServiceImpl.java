package com.example.taskmanager.service;

import com.example.taskmanager.dto.TaskDTO;
import com.example.taskmanager.dto.UserDTO;
import com.example.taskmanager.dto.WebTaskDTO;
import com.example.taskmanager.entity.Task;
import com.example.taskmanager.entity.User;
import com.example.taskmanager.mapper.TaskMapper;
import com.example.taskmanager.mapper.UserMapper;
import com.example.taskmanager.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of the TaskService interface for managing tasks.
 */
@Service
public class TaskServiceImpl implements TaskService {

	private final TaskRepository taskRepository;
	private final TaskMapper taskMapper;
	private final UserService userService;
	private final UserMapper userMapper;

	/**
	 * Constructs a new instance of {@code TaskServiceImpl}.
	 *
	 * @param taskRepository the repository to manage tasks
	 * @param taskMapper the mapper to convert between Task entities and DTOs
	 * @param userService the service to manage user-related operations
	 * @param userMapper the mapper to convert between User entities and DTOs
	 */
	@Autowired
	public TaskServiceImpl(TaskRepository taskRepository, TaskMapper taskMapper,
						   UserService userService, UserMapper userMapper) {
		this.taskRepository = taskRepository;
		this.taskMapper = taskMapper;
		this.userService = userService;
		this.userMapper = userMapper;

	}


	/**
	 * Retrieves a task by its unique identifier.
	 *
	 * @param id The unique identifier of the task.
	 * @return An {@code Optional} containing the {@link TaskDTO} if the task is found, otherwise an empty {@code Optional}.
	 */
	@Override
	public Optional<TaskDTO> getTaskById(int id) {
		return taskRepository.findById(id)
				.map(taskMapper::toDTO);
	}

	/**
	 * Retrieves all tasks from the repository and converts them to TaskDTO objects.
	 *
	 * @return a list of TaskDTO objects representing all tasks.
	 */
	@Override
	public List<TaskDTO> getAllTasks() {
		return taskMapper.toDTO(taskRepository.findAll());
	}

	/**
	 * Creates a new task for the specified user.
	 *
	 * @param webTaskDTO the task data transfer object containing the task details
	 * @param username the username of the user for whom the task is being created
	 * @return the created TaskDTO
	 * @throws RuntimeException if the user is not found
	 */
	@Override
	@Transactional
	public TaskDTO createTask(WebTaskDTO webTaskDTO, String username) {
		User user = userService.getUserByUsername(username);
		if (user == null) {
			throw new RuntimeException("User not found with username" + username);
		}

		Task task = Task.builder()
				.taskName(webTaskDTO.getTaskName())
				.startDate(webTaskDTO.getStartDate())
				.dueDate(webTaskDTO.getDueDate())
				.user(user)
				.taskStatus(false)
				.build();

		taskRepository.save(task);

		return taskMapper.toDTO(task);

	}

	/**
	 * Deletes a task by its identifier.
	 * This method retrieves the task by its id, and if found, deletes it from the repository.
	 *
	 * @param id the identifier of the task to be deleted
	 * @throws RuntimeException if the task with the specified id is not found
	 */
	@Override
	@Transactional
	public void deleteTask(int id) {

		Task task = taskRepository.findById(id)
						.orElseThrow(() -> new RuntimeException("Task with id : " + id + " not found."));

		taskRepository.delete(task);
	}

	/**
	 * Retrieves a task based on its start date.
	 *
	 * @param startDate the start date of the task to be retrieved
	 * @return an Optional containing the TaskDTO if a task with the specified start date exists, otherwise an empty Optional
	 */
	@Override
	public Optional<TaskDTO> getTaskByStartDate(LocalDate startDate) {

		return taskRepository.findByStartDate(startDate)
				.map(taskMapper::toDTO);
	}

	/**
	 * Retrieves a task by its due date.
	 *
	 * @param dueDate the due date of the task to be retrieved
	 * @return an Optional containing the TaskDTO if found, otherwise an empty Optional
	 */
	@Override
	public Optional<TaskDTO> getTaskByDueDate(LocalDate dueDate) {

		return taskRepository.findByDueDate(dueDate)
				.map(taskMapper::toDTO);
	}

	/**
	 * Toggles the status of a task identified by its ID.
	 *
	 * @param id the ID of the task whose status is to be toggled
	 * @return the new status of the task after toggling
	 * @throws RuntimeException if the task with the given ID is not found
	 */
	@Override
	@Transactional
	public boolean toggleTaskStatus(int id) {

		Task task = taskRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Task with id: " + id + " not found."));

		task.setTaskStatus(!task.isTaskStatus());

		taskRepository.save(task);

		return task.isTaskStatus();
	}

	/**
	 * Retrieves a list of tasks for a specified user by their user ID.
	 *
	 * @param id the user ID to fetch tasks for
	 * @return a list of TaskDTO objects associated with the specified user
	 */
	@Override
	public List<TaskDTO> getTasksByUserId(int id) {
		return taskRepository.findTasksByUserId(id)
				.stream().map(taskMapper::toDTO)
				.collect(Collectors.toList());
	}
}
