package com.example.taskmanager.config;

import com.example.taskmanager.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuration for application security.
 * <p>
 * This class configures the security settings, authentication mechanisms,
 * password encoding, and access control for different application endpoints.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private final CustomUserDetailsService customUserDetailsService;
	private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

	/**
	 * Constructs a SecurityConfig instance with the specified CustomUserDetailsService
	 * and CustomAuthenticationSuccessHandler.
	 *
	 * @param customUserDetailsService           the service used to load user-specific data
	 * @param customAuthenticationSuccessHandler the handler for authentication success events
	 */
	@Autowired
	public SecurityConfig(CustomUserDetailsService customUserDetailsService,
						  CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler) {
		this.customUserDetailsService = customUserDetailsService;
		this.customAuthenticationSuccessHandler = customAuthenticationSuccessHandler;
	}


	/**
	 * Provides a password encoder bean for hashing passwords.
	 * <p>
	 * This method returns a {@link BCryptPasswordEncoder} instance,
	 * which is used to encode passwords in a secure manner before storing them.
	 *
	 * @return a {@link BCryptPasswordEncoder} instance for password hashing
	 */
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/**
	 * Configures and returns a DaoAuthenticationProvider bean.
	 * <p>
	 * The DaoAuthenticationProvider uses the provided CustomUserDetailsService
	 * to retrieve user details for authentication and the configured password encoder for password matching.
	 *
	 * @param customUserDetailsService the service to load user-specific data
	 * @return a fully configured DaoAuthenticationProvider
	 */
	@Bean
	public DaoAuthenticationProvider daoAuthenticationProvider(CustomUserDetailsService customUserDetailsService) {
		DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
		auth.setUserDetailsService(customUserDetailsService);
		auth.setPasswordEncoder(passwordEncoder());

		return auth;
	}


	/**
	 * Configures the security filter chain for the application, defining which
	 * endpoints require authentication, the login and logout processes, and how
	 * to handle access denied scenarios.
	 *
	 * @param http the HttpSecurity to configure.
	 * @return the built SecurityFilterChain.
	 * @throws Exception if an error occurs while configuring the HttpSecurity.
	 */
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http.authorizeHttpRequests(configurer ->
						configurer
								.requestMatchers("/css/**", "/images/**").permitAll()
								.requestMatchers("/", "/register-new-user", "/login", "/logout", "/access-denied", "/api/register-user").permitAll()
								.requestMatchers("/dashboard", "/create-task", "/delete-tasks", "/update-task-status", "/update-user-info").authenticated()
								.requestMatchers("/api/**").hasAnyRole("USER", "ADMIN")
								.requestMatchers("/admin-dashboard", "/admin-user-tasks", "/admin-create-user-task", "/api/admin/**").hasRole("ADMIN")
								.anyRequest().authenticated()
				)
				.formLogin(form ->
						form
								.loginPage("/login")
								.loginProcessingUrl("/authenticateUser")
								.successHandler(customAuthenticationSuccessHandler)
								.permitAll())
				.logout(logout ->
						logout
								.logoutUrl("/logout")
								.logoutSuccessUrl("/")
								.invalidateHttpSession(true)
								.deleteCookies("JSESSIONID")
								.permitAll())
				.anonymous(anonymous -> anonymous.disable())
				.exceptionHandling(configurer ->
						configurer
								.accessDeniedPage("/access-denied"));
		return http.build();
	}

	/**
	 * Configures and returns an AuthenticationManager for the application.
	 * This method uses the provided HttpSecurity object to obtain an
	 * AuthenticationManagerBuilder and configures it to use a custom
	 * UserDetailsService and a password encoder.
	 *
	 * @param http the HttpSecurity object used to configure the AuthenticationManager
	 * @return the configured AuthenticationManager
	 * @throws Exception if an error occurs while configuring the AuthenticationManager
	 */
	@Bean
	public AuthenticationManager authManager(HttpSecurity http) throws Exception {
		AuthenticationManagerBuilder authenticationManagerBuilder =
				http.getSharedObject(AuthenticationManagerBuilder.class);
		authenticationManagerBuilder.authenticationProvider(daoAuthenticationProvider(customUserDetailsService));
		return authenticationManagerBuilder.build();
	}


}
