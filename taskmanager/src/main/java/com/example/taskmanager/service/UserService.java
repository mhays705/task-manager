package com.example.taskmanager.service;

import com.example.taskmanager.dto.UserDTO;
import com.example.taskmanager.dto.WebUserDTO;
import com.example.taskmanager.entity.User;
import jakarta.servlet.http.HttpSession;

import java.util.List;
import java.util.Optional;

public interface UserService {

	UserDTO registerUser(WebUserDTO webUserDTO);

	Optional<UserDTO> getUserByUsername(String username);

	Optional<UserDTO> getUserById(int id);

	Optional<UserDTO> updateUser(int id, WebUserDTO user);

	List<UserDTO> getAllUsers();

	boolean deleteUser(int id);

	void login(User user, HttpSession session);

	public User getCurrentUser(HttpSession session);
}
