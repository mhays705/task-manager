package com.example.taskmanager.dto;



import com.example.taskmanager.validation.OnCreate;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a Data Transfer Object (DTO) for web user information.
 *
 * This class is used to capture and validate user data during web interactions,
 * typically during registration or updating user details. It includes various
 * validation annotations to ensure the integrity and correctness of the data
 * provided by the user.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WebUserDTO {


	@NotBlank(message = "Username is required")
	@Size(min = 2, max = 45, message = "Username must be 5 - 45 characters")
	private String username;

	@NotBlank(message = "First name is required")
	@Size(min = 1, max = 45, message = "First name must be 1 - 45 characters")
	private String firstName;

	@NotBlank(message = "Last name is required")
	@Size(min = 1, max = 45, message = "Last name must be 1 - 45 characters")
	private String lastName;

	@NotBlank(message = "Email is required")
	@Email(message = "Enter valid email address")
	@Size(max = 255, message = "Must be less than 255 characters")
	private String email;

	@NotBlank(groups = OnCreate.class, message = "Password is required")
	@Size(min = 5, message = "password must be at least 5 characters ")
	private String password;

//	@NotBlank(message = "is required")
//	@Size(min = 5, max = 40, message = "Password confirmation must match password")
//	private String passwordConfirmation;




}
