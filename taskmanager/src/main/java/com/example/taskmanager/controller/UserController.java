package com.example.taskmanager.controller;

import com.example.taskmanager.dto.UserDTO;
import com.example.taskmanager.dto.WebUserDTO;
import com.example.taskmanager.service.UserService;
import com.example.taskmanager.validation.OnCreate;
import com.example.taskmanager.validation.OnUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {

	private final UserService userService;

	/**
	 * Constructs a UserController with the specified UserService.
	 *
	 * The UserService is injected through constructor-based dependency injection.
	 *
	 * @param userService an instance of UserService to be used by this controller
	 */
	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}


	/**
	 * Registers a new user with the provided user details.
	 *
	 * @param webUserDTO the DTO containing web user information for registration
	 * @param bindingResult the binding result holding validation errors if any
	 * @param redirectAttributes the attributes used for redirecting with flash messages
	 * @return a string representing the view name or redirect URL
	 */
	@PostMapping("/register-new-user")
	public String registerUser(@Validated(OnCreate.class) @ModelAttribute("webUserDTO") WebUserDTO webUserDTO,
							   BindingResult bindingResult,
							   RedirectAttributes redirectAttributes) {

		if (bindingResult.hasErrors()) {
			redirectAttributes.addFlashAttribute("webUserDTo" ,webUserDTO);
			redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.webUserDTO", bindingResult);
			return "user-registration-form";
		}

		try {
			userService.registerUser(webUserDTO);
			redirectAttributes.addFlashAttribute("successMessage", "User registered successfully.");
			return "redirect:/login";
		}
		catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "Registration Failed " + e.getMessage());
			return "register-new-user";
		}

	}

	/**
	 * Handles the user information update request through a PATCH mapping.
	 *
	 * This method validates the user-provided data, updates the user information if the data is valid,
	 * and sets appropriate flash attributes to provide feedback to the user.
	 *
	 * @param webUserDTO the data transfer object containing updated user information
	 * @param bindingResult the object holding the results of the validation and binding errors
	 * @param redirectAttributes the object to add attributes for the redirect scenario
	 * @return a string representing the view name to be rendered or the redirect target
	 */
	@PatchMapping("/update-user-info")
	public String updateUserInfo(@Validated(OnUpdate.class) @ModelAttribute("webUserDTO")WebUserDTO webUserDTO,
								 BindingResult bindingResult,
								 RedirectAttributes redirectAttributes) {
		if (bindingResult.hasErrors()) {
			return "update-user-info";
		}
		Optional<UserDTO> updatedUser = userService.updateUser(webUserDTO);

		if (updatedUser.isEmpty()) {
			redirectAttributes.addFlashAttribute("error", "Update of user has failed.");
			return "redirect:/dashboard";
		}

		redirectAttributes.addFlashAttribute("message", "User successfully updated.");
		return "redirect:/dashboard";
	}






}
