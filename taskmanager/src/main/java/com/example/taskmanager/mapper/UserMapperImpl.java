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

/**
 * Implementation of the UserMapper interface, providing methods
 * to map between User entities and UserDTO data transfer objects.
 * This class uses a TaskMapper to handle the mapping of associated tasks.
 */
@Component
public class UserMapperImpl implements UserMapper {


	private final TaskMapper taskMapper;

	/**
	 * Constructs a UserMapperImpl with the specified TaskMapper.
	 *
	 * @param taskMapper the TaskMapper used for mapping Task objects within User entities and DTOs
	 */
	@Autowired
	public UserMapperImpl(TaskMapper taskMapper) {
		this.taskMapper = taskMapper;
	}

	/**
	 * Converts a User entity to a UserDTO.
	 *
	 * @param user the User entity to be converted
	 * @return the resulting UserDTO
	 */
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

	/**
	 * Converts an iterable collection of User entities to a list of UserDTO objects.
	 *
	 * @param users an iterable collection of User entities to be converted
	 * @return a list of UserDTO objects corresponding to the User entities
	 */
	@Override
	public List<UserDTO> toDTO(Iterable<User> users) {
		List<UserDTO> userDTOS = new ArrayList<>();

		for (User user : users) {
			userDTOS.add(toDTO(user));
		}

		return userDTOS;
	}

	/**
	 * Converts a UserDTO object to a User entity.
	 *
	 * @param userDTO the UserDTO object to convert. If null, the method returns null.
	 * @return the converted User entity. If the input is null, returns null.
	 */
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

