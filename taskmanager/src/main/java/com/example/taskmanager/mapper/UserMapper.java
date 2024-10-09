package com.example.taskmanager.mapper;

import com.example.taskmanager.dto.UserDTO;
import com.example.taskmanager.entity.User;

import java.util.List;

public interface UserMapper {

	UserDTO toDTO(User user);

	User toEntity(UserDTO userDTO);

	List<UserDTO> toDTO(Iterable<User> users);

}
