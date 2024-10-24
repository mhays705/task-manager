package com.example.taskmanager.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * CustomAuthenticationSuccessHandler is an implementation of the AuthenticationSuccessHandler interface.
 *
 * This class handles the scenario when a user has successfully authenticated. Based on the
 * authenticated user's roles, it determines the appropriate redirect URL. If the user has an
 * "ROLE_ADMIN" authority, they are redirected to the admin dashboard; otherwise, they are redirected
 * to the general dashboard.
 */
@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	/**
	 * Handles the scenario when a user has successfully authenticated.
	 * Based on the authenticated user's roles, it determines the appropriate redirect URL.
	 * If the user has an "ROLE_ADMIN" authority, they are redirected to the admin dashboard;
	 * otherwise, they are redirected to the general dashboard.
	 *
	 * @param request the HttpServletRequest object.
	 * @param response the HttpServletResponse object.
	 * @param authentication the Authentication object containing user authentication information.
	 * @throws IOException if an input or output exception occurs.
	 * @throws ServletException if a servlet-specific exception occurs.
	 */
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
										Authentication authentication) throws IOException, ServletException {
		String redirectUrl;


		if (authentication.getAuthorities().stream()
				.anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"))) {
			redirectUrl = "/admin/dashboard";
		}
		else {
			redirectUrl = "/dashboard";
		}

		response.sendRedirect(redirectUrl);
	}
}