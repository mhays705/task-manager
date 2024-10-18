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

/**
 * Service implementation for handling user-related operations.
 */
@Service
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final RoleService roleService;
	private final UserMapper userMapper;

	/**
	 * Constructs a new instance of UserServiceImpl.
	 *
	 * @param userRepository the repository for user data
	 * @param passwordEncoder the encoder for handling user passwords
	 * @param roleService service for managing roles
	 * @param userMapper the mapper for converting between User and UserDTO entities
	 */
	@Autowired
	public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder,
						   RoleService roleService, UserMapper userMapper) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.roleService = roleService;
		this.userMapper = userMapper;
	}


	/**
	 * Registers a new user by saving their information to the database after validating
	 * that the email and username do not already exist.
	 *
	 * @param webUserDTO the data transfer object containing user registration information
	 * @return the data transfer object representing the registered user
	 */
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

	/**
	 * Retrieves a UserDTO object by the given username.
	 *
	 * @param username the username of the user to be retrieved
	 * @return an Optional containing the UserDTO if found, or an empty Optional if not found
	 */
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

	/**
	 * Retrieves a User object by its username.
	 *
	 * @param username the username to search for
	 * @return the User object associated with the given username
	 */
	@Override
	public User getUserByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	/**
	 * Retrieves a user by their unique identifier.
	 *
	 * @param id the unique identifier of the user
	 * @return an optional containing the UserDTO if found, or an empty optional if not found
	 */
	@Override
	public Optional<UserDTO> getUserById(int id) {
		return userRepository.findById(id)
				.map(userMapper::toDTO);
	}

	/**
	 * Updates the user corresponding to the provided WebUserDTO.
	 *
	 * @param webUserDTO Data Transfer Object containing the updated user information.
	 * @return An Optional containing the updated UserDTO if the user is successfully updated;
	 *         otherwise, throws a RuntimeException if the user is not found.
	 */
	@Override
	@Transactional
	public Optional<UserDTO> updateUser(WebUserDTO webUserDTO) {
	User user = userRepository.findByUsername(webUserDTO.getUsername());
	if (user == null) {
		throw new RuntimeException("User not found.");
	}
	updateFields(user, webUserDTO);

	userRepository.save(user);

	UserDTO userDTO = userMapper.toDTO(user);

	return Optional.of(userDTO);

	}




	/**
	 * Updates the fields of an existing User entity based on the non-null values from a WebUserDTO.
	 *
	 * @param existingUser The User entity to update.
	 * @param webUserDTO The WebUserDTO containing updated field values. Non-null and unequal values
	 * in this DTO will be used to update the fields of the existing user.
	 */
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

	/**
	 * Retrieves all user records from the database and converts them into a list of UserDTO objects.
	 *
	 * @return a list of UserDTO objects representing all users in the system.
	 */
	@Override
	public List<UserDTO> getAllUsers() {
		return userMapper.toDTO(userRepository.findAll());
	}

	/**
	 * Deletes a user by their ID.
	 *
	 * @param id the unique identifier of the user to be deleted
	 * @return true if the user was successfully deleted, false if the user was not found
	 */
	@Override
	@Transactional
	public boolean deleteUser(int id) {
		if (!userRepository.existsById(id)) {
			return false;
		}
		userRepository.deleteById(id);
		return true;
	}

}
