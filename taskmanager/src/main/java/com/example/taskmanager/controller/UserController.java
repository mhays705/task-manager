package com.example.taskmanager.controller;

import com.example.taskmanager.dto.UserDTO;
import com.example.taskmanager.dto.WebUserDTO;
import com.example.taskmanager.entity.User;
import com.example.taskmanager.service.UserService;
import com.example.taskmanager.validation.OnCreate;
import com.example.taskmanager.validation.OnUpdate;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UserController {

	private final UserService userService;

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/user/{id}")
	public ResponseEntity<UserDTO> getUserById(@PathVariable int id) {
		return userService.getUserById(id)
				.map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@GetMapping("/user/by-username/{username}")
	public ResponseEntity<UserDTO> getUserByUsername(@PathVariable String username) {
		return userService.getUserDTOByUsername(username)
				.map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@GetMapping("/users")
	public ResponseEntity<List<UserDTO>> getAllUsers() {
		List<UserDTO> users = userService.getAllUsers();
		if (users.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(users);
	}

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

    @PatchMapping("/user/update-user-info")
    public ResponseEntity<String> updateUser(@Validated(OnUpdate.class) @ModelAttribute WebUserDTO webUserDTO) {
        Optional<UserDTO> updateUser = userService.updateUser(webUserDTO);

        if (updateUser.isEmpty()) {
            return ResponseEntity.badRequest().body("Update has failed")	;
        }


        return ResponseEntity.status(HttpStatus.FOUND).header(HttpHeaders.LOCATION, "/dashboard").body("Update successful");
    }


}
