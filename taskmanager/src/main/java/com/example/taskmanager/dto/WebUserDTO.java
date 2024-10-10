package com.example.taskmanager.dto;



import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WebUserDTO {


	@NotBlank(message = "is required")
	@Size(min = 2, max = 45, message = "Username must be 5 - 45 characters")
	private String username;

	@NotBlank(message = "is required")
	@Size(min = 1, max = 45, message = "First name must be 1 - 45 characters")
	private String firstName;

	@NotBlank(message = "is required")
	@Size(min = 1, max = 45, message = "Last name must be 1 - 45 characters")
	private String lastName;

	@NotBlank(message = "is required")
	@Email(message = "Enter valid email address")
	@Size(max = 255, message = "Must be less than 255 characters")
	private String email;

	@NotBlank(message = "is required")
	@Size(min = 5, message = "password must be at least 5 characters ")
	private String password;

//	@NotBlank(message = "is required")
//	@Size(min = 5, max = 40, message = "Password confirmation must match password")
//	private String passwordConfirmation;




}