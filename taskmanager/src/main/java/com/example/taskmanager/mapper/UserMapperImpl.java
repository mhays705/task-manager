package com.example.taskmanager.mapper;

import com.example.taskmanager.dto.TaskDTO;
import com.example.taskmanager.dto.UserDTO;
import com.example.taskmanager.entity.User;
import com.example.taskmanager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapperImpl implements UserMapper {


	private final TaskMapper taskMapper;

	@Autowired
	public UserMapperImpl(TaskMapper taskMapper) {
		this.taskMapper = taskMapper;
	}

	@Override
	public UserDTO toDTO(User user) {


        return UserDTO.builder()
				.username(user.getUsername())
				.firstName(user.getFirstName())
				.lastName(user.getLastName())
				.email(user.getEmail())
				.roles(user.getRoles())
				.tasks(user.getTasks())
				.build();
	}

	@Override
	public List<UserDTO> toDTO(Iterable<User> users) {
		List<UserDTO> userDTOS = new ArrayList<>();

		for (User user : users) {
			userDTOS.add(toDTO(user));
		}

		return userDTOS;
	}

	@Override
	public User toEntity(UserDTO userDTO) {

		if (userDTO == null) {
			return null;
		}
		User user = User.builder()
				.id(userDTO.getId())
				.username(userDTO.getUsername())
				.firstName(userDTO.getFirstName())
				.lastName(userDTO.getLastName())
				.email(userDTO.getEmail())
				.roles(userDTO.getRoles())
				.tasks(userDTO.getTasks())
				.build();

		return user;
	}


}

