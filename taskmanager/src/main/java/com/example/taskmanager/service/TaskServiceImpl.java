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

@Service
public class TaskServiceImpl implements TaskService {

	private final TaskRepository taskRepository;
	private final TaskMapper taskMapper;
	private final UserService userService;
	private final UserMapper userMapper;

	@Autowired
	public TaskServiceImpl(TaskRepository taskRepository, TaskMapper taskMapper,
						   UserService userService, UserMapper userMapper) {
		this.taskRepository = taskRepository;
		this.taskMapper = taskMapper;
		this.userService = userService;
		this.userMapper = userMapper;

	}


	@Override
	public Optional<TaskDTO> getTaskById(int id) {
		return taskRepository.findById(id)
				.map(taskMapper::toDTO);
	}

	@Override
	public List<TaskDTO> getAllTasks() {
		return taskMapper.toDTO(taskRepository.findAll());
	}

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

	@Override
	@Transactional
	public void deleteTask(int id) {

		Task task = taskRepository.findById(id)
						.orElseThrow(() -> new RuntimeException("Task with id : " + id + " not found."));

		taskRepository.delete(task);
	}

	@Override
	public Optional<TaskDTO> getTaskByStartDate(LocalDate startDate) {

		return taskRepository.findByStartDate(startDate)
				.map(taskMapper::toDTO);
	}

	@Override
	public Optional<TaskDTO> getTaskByDueDate(LocalDate dueDate) {

		return taskRepository.findByDueDate(dueDate)
				.map(taskMapper::toDTO);
	}

	@Override
	@Transactional
	public boolean toggleTaskStatus(int id) {

		Task task = taskRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Task with id: " + id + " not found."));

		task.setTaskStatus(!task.isTaskStatus());

		taskRepository.save(task);

		return task.isTaskStatus();
	}
}
