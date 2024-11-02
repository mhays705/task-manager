package com.example.taskmanager.controller;

import com.example.taskmanager.dto.UserDTO;
import com.example.taskmanager.dto.WebUserDTO;
import com.example.taskmanager.service.UserService;
import com.example.taskmanager.validation.OnCreate;
import com.example.taskmanager.validation.OnPasswordUpdate;
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
	 * <p>
	 * The UserService is injected through constructor-based dependency injection.
	 *
	 * @param userService an instance of UserService to be used by this controller
	 */
	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}


	/**
	 * Registers a new user based on the provided WebUserDTO.
	 * <p>
	 * This method validates the incoming user data, registers the user
	 * if valid, and redirects appropriately with success or error messages.
	 *
	 * @param webUserDTO         the DTO containing web user information to be registered
	 * @param bindingResult      the binding result holding validation errors if any
	 * @param redirectAttributes the attributes used for redirecting with flash messages
	 * @return a string representing the view name or redirect URL
	 */
	@PostMapping("/register-new-user")
	public String registerUser(@Validated(OnCreate.class) @ModelAttribute("webUserDTO") WebUserDTO webUserDTO,
							   BindingResult bindingResult,
							   RedirectAttributes redirectAttributes) {
		if (!webUserDTO.getPassword().equals(webUserDTO.getPasswordConfirmation())) {
			bindingResult.rejectValue("passwordConfirmation", null, "Passwords do not match.");
		}

		if (bindingResult.hasErrors()) {
			return "user-registration-form";
		}

		try {
			userService.registerUser(webUserDTO);
			redirectAttributes.addFlashAttribute("successMessage", "User registered successfully.");
			return "redirect:/login";
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "Registration Failed " + e.getMessage());
			return "register-new-user";
		}

	}


	/**
	 * Updates the user information based on the provided web user DTO.
	 * <p>
	 * This method validates the incoming user data, updates the user information
	 * if valid, and redirects to the dashboard with appropriate success or error messages.
	 *
	 * @param webUserDTO         the DTO containing web user information to be updated
	 * @param bindingResult      the binding result holding validation errors if any
	 * @param redirectAttributes the attributes used for redirecting with flash messages
	 * @return a string representing the view name or redirect URL
	 */
	@PatchMapping("/update-user-info")
	public String updateUserInfo(@Validated(OnUpdate.class) @ModelAttribute("webUserDTO") WebUserDTO webUserDTO,
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

	/**
	 * Updates the password of a user based on the provided WebUserDTO.
	 * <p>
	 * This method validates the incoming password data, checks for matching passwords,
	 * and performs the password update if valid. It then redirects to the dashboard with
	 * appropriate success or error messages.
	 *
	 * @param webUserDTO         the DTO containing web user password information to be updated
	 * @param bindingResult      the binding result holding validation errors if any
	 * @param redirectAttributes the attributes used for redirecting with flash messages
	 * @return a string representing the view name or redirect URL to the dashboard
	 */
	@PatchMapping("/update-password")
	public String updatePassword(@Validated(OnPasswordUpdate.class) @ModelAttribute("webUserDTO") WebUserDTO webUserDTO,
								 BindingResult bindingResult,
								 RedirectAttributes redirectAttributes) {

		if (!webUserDTO.getPassword().equals(webUserDTO.getPasswordConfirmation())) {
			bindingResult.rejectValue("passwordConfirmation", null, "Passwords do not match.");
		}

		if (bindingResult.hasErrors()) {
			return "update-password-form";
		}


		Optional<UserDTO> updatedUser;
		try {

			updatedUser = userService.updatePassword(webUserDTO);
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
			return "redirect:/update-password";
		}

		if (updatedUser.isEmpty()) {
			redirectAttributes.addFlashAttribute("error", "Error while updating password");
			return "redirect:/dashboard";
		}

		redirectAttributes.addFlashAttribute("successMessage", "Password successfully updated.");
		return "redirect:/dashboard";
	}


}
