package com.example.taskmanager.rest;

import com.example.taskmanager.dto.UserDTO;
import com.example.taskmanager.dto.WebUserDTO;
import com.example.taskmanager.service.UserService;
import com.example.taskmanager.validation.OnCreate;
import com.example.taskmanager.validation.OnUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * UserController handles HTTP requests related to user operations.
 *
 * This class is a REST controller that provides endpoints for retrieving user information,
 * registering users, and updating user details. It interacts with the UserService to process
 * the requests and return appropriate responses.
 */
@RestController
@RequestMapping("/api")
public class UserRestController {

	private final UserService userService;

	/**
	 * Constructs a new UserController with the specified UserService.
	 *
	 * @param userService the service used by this controller to handle user-related operations.
	 */
	@Autowired
	public UserRestController(UserService userService) {
		this.userService = userService;
	}

	/**
	 * Retrieves a user by their unique identifier.
	 *
	 * This method handles HTTP GET requests for fetching user information based on the user's ID.
	 * It interacts with the UserService to retrieve the user's details and returns an HTTP response
	 * with the user data if found, or a 404 Not Found status if the user does not exist.
	 *
	 * @param id the unique identifier of the user to be retrieved
	 * @return a ResponseEntity containing the UserDTO if the user is found,
	 *         or a 404 Not Found status if the user does not exist
	 */
	@GetMapping("/user/{id}")
	public ResponseEntity<UserDTO> getUserById(@PathVariable int id) {
		return userService.getUserById(id)
				.map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	/**
	 * Retrieves user information based on the provided username.
	 *
	 * @param username the username of the user to be retrieved
	 * @return ResponseEntity containing the UserDTO if the user is found, or a 404 Not Found status if not
	 */
	@GetMapping("/user/by-username/{username}")
	public ResponseEntity<UserDTO> getUserByUsername(@PathVariable String username) {
		return userService.getUserDTOByUsername(username)
				.map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	/**
	 * Retrieves a list of all users.
	 *
	 * This method handles HTTP GET requests to the "/users" endpoint.
	 * It interacts with the UserService to fetch a list of UserDTO objects,
	 * representing all the users in the system. If no users are found, it
	 * returns a "204 No Content" status. Otherwise, it returns a "200 OK"
	 * status along with the list of users.
	 *
	 * @return ResponseEntity containing the list of UserDTOs or a "204 No Content" status.
	 */
	@GetMapping("/users")
	public ResponseEntity<List<UserDTO>> getAllUsers() {
		List<UserDTO> users = userService.getAllUsers();
		if (users.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(users);
	}

	/**
	 * Registers a new user.
	 *
	 * This method handles the HTTP POST request to register a new user. It takes user data from a web form,
	 * validates it, and attempts to register the user. If the validation fails or registration is unsuccessful,
	 * appropriate HTTP status codes are returned.
	 *
	 * @param webUser The user data submitted via the web form for registration.
	 * @param bindingResult The result of the validation of the user data.
	 * @return A ResponseEntity containing the status of the registration process.
	 */
	@PostMapping("/register-user")
	public ResponseEntity<String> registerUser(@Validated(OnCreate.class) @ModelAttribute WebUserDTO webUser, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			return ResponseEntity.badRequest().build();
		}

		UserDTO newUser = userService.registerUser(webUser);

		if (newUser == null) {
			return ResponseEntity.badRequest().build();
		}

		return ResponseEntity.status(HttpStatus.FOUND).header(HttpHeaders.LOCATION, "/dashboard").build();
	}

    /**
	 * Updates the user information.
	 *
	 * This method handles the HTTP PATCH request to update the user information based on the data
	 * provided in the WebUserDTO. It uses the UserService to update the user details and returns
	 * appropriate HTTP response entities based on the success or failure of the update operation.
	 *
	 * @param webUserDTO the data transfer object containing updated user information
	 * @return a ResponseEntity containing the result of the update operation, with status and message
	 */
	@PatchMapping("/user/update-user-info")
    public ResponseEntity<String> updateUser(@Validated(OnUpdate.class) @ModelAttribute WebUserDTO webUserDTO) {
        Optional<UserDTO> updateUser = userService.updateUser(webUserDTO);

        if (updateUser.isEmpty()) {
            return ResponseEntity.badRequest().body("Update has failed")	;
        }


        return ResponseEntity.status(HttpStatus.FOUND).header(HttpHeaders.LOCATION, "/dashboard").body("Update successful");
    }


}
