package com.example.taskmanager.service;

import com.example.taskmanager.dto.UserDTO;
import com.example.taskmanager.dto.WebUserDTO;
import com.example.taskmanager.entity.User;
import com.example.taskmanager.mapper.UserMapper;
import com.example.taskmanager.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final RoleService roleService;
	private final UserMapper userMapper;

	@Autowired
	public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder,
						   RoleService roleService, UserMapper userMapper) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.roleService = roleService;
		this.userMapper = userMapper;
	}


	@Override
	@Transactional
	public UserDTO registerUser(WebUserDTO webUserDTO) {

		if (userRepository.existsByEmail(webUserDTO.getEmail()) || userRepository.existsByUsername(webUserDTO.getUsername())) {
			throw new RuntimeException("Username or Email already exists.");
		}

		User user = User.builder()
				.username(webUserDTO.getUsername())
				.firstName((webUserDTO.getFirstName()))
				.lastName(webUserDTO.getLastName())
				.email(webUserDTO.getEmail())
				.roles(Set.of(roleService.getUserRole()))
				.password(passwordEncoder.encode(webUserDTO.getPassword()))
				.enabled(true)
				.build();

		userRepository.save(user);

		return userMapper.toDTO(user);
	}

	@Override
	public Optional<UserDTO> getUserDTOByUsername(String username) {
		User user =userRepository.findByUsername(username);
				if (user != null) {
					return Optional.of(userMapper.toDTO(user));
				}
				else {
					return Optional.empty();
				}
	}

	@Override
	public User getUserByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	@Override
	public Optional<UserDTO> getUserById(int id) {
		return userRepository.findById(id)
				.map(userMapper::toDTO);
	}

	@Override
	@Transactional
	public Optional<UserDTO> updateUser(int id, WebUserDTO webUserDTO) {
		return userRepository.findById(id).map(existingUser -> {
			updateFields(existingUser, webUserDTO);
			userRepository.save(existingUser);
			return userMapper.toDTO(existingUser);
		});


	}

	/** Helper method for updateUser */
	@Transactional
	public void updateFields(User existingUser, WebUserDTO webUserDTO) {
		if (webUserDTO.getUsername() != null && !webUserDTO.getUsername().equals(existingUser.getUsername())) {
			existingUser.setUsername(webUserDTO.getUsername());
		}
		if (webUserDTO.getFirstName() != null && !webUserDTO.getFirstName().equals(existingUser.getFirstName())) {
			existingUser.setFirstName(webUserDTO.getFirstName());
		}
		if (webUserDTO.getLastName() != null && !webUserDTO.getLastName().equals(existingUser.getLastName())) {
			existingUser.setLastName(webUserDTO.getLastName());
		}
		if (webUserDTO.getEmail() != null && !webUserDTO.getEmail().equals(existingUser.getEmail())) {
			existingUser.setEmail(webUserDTO.getEmail());
		}
	}

	@Override
	public List<UserDTO> getAllUsers() {
		return userMapper.toDTO(userRepository.findAll());
	}

	@Override
	@Transactional
	public boolean deleteUser(int id) {
		if (!userRepository.existsById(id)) {
			return false;
		}
		userRepository.deleteById(id);
		return true;
	}

	public void login(User user, HttpSession session) {
		session.setAttribute("currentUser", user);
	}

	public User getCurrentUser(HttpSession session) {
		return (User) session.getAttribute("currentUser"); // Ensure you set this attribute when the user logs in
	}
}
