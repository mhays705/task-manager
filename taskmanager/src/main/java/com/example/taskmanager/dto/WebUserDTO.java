package com.example.taskmanager.dto;



import com.example.taskmanager.entity.Role;
import com.example.taskmanager.validation.OnCreate;
import com.example.taskmanager.validation.OnUpdate;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;


/**
 * Data Transfer Object (DTO) for capturing web user information.
 *
 * This class is used for transferring user data between web forms and the backend.
 * It includes validation annotations to enforce mandatory fields and constraints
 * on the length and format of the input data.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WebUserDTO {


	@NotBlank(groups = {OnCreate.class, OnUpdate.class}, message = "Username is required")
	@Size(groups = {OnCreate.class, OnUpdate.class}, min = 5, max = 45, message = "Username must be 5 - 45 characters")
	private String username;

	@NotBlank(groups = {OnCreate.class, OnUpdate.class}, message = "First name is required")
	@Size(groups = {OnCreate.class, OnUpdate.class}, min = 1, max = 45, message = "First name must be 1 - 45 characters")
	private String firstName;

	@NotBlank(groups = {OnCreate.class, OnUpdate.class}, message = "Last name is required")
	@Size(groups = {OnCreate.class, OnUpdate.class}, min = 1, max = 45, message = "Last name must be 1 - 45 characters")
	private String lastName;

	@NotBlank(groups = {OnCreate.class, OnUpdate.class}, message = "Email is required")
	@Email(groups = {OnCreate.class, OnUpdate.class}, message = "Enter valid email address")
	@Size(groups = {OnCreate.class, OnUpdate.class}, max = 255, message = "Must be less than 255 characters")
	private String email;

	@NotBlank(groups = OnCreate.class, message = "Password is required")
	@Size(groups = OnCreate.class,min = 5, message = "Password must be at least 5 characters ")
	private String password;

	@NotBlank(groups = OnCreate.class, message = "Password confirmation is required")
	private String passwordConfirmation;


	private Set<Role> roles;


}
