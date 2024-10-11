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

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private final CustomUserDetailsService customUserDetailsService;

	@Autowired
	public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
		this.customUserDetailsService = customUserDetailsService;
	}


	@Bean
	public BCryptPasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}

	@Bean
	public DaoAuthenticationProvider daoAuthenticationProvider(CustomUserDetailsService customUserDetailsService) {
		DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
		auth.setUserDetailsService(customUserDetailsService);
		auth.setPasswordEncoder(passwordEncoder());

		return auth;
	}

	/** ClEAN UP LATER */
//	@Bean
//	public UserDetailsManager userDetailsManager(DataSource dataSource) {
//
//		JdbcUserDetailsManager manager = new JdbcUserDetailsManager(dataSource);
//
//		manager
//				.setUsersByUsernameQuery("SELECT id, password, enabled FROM users where username=?");
//		manager
//				.setAuthoritiesByUsernameQuery("SELECT r.name FROM roles r " +
//						"JOIN user_roles ur ON r.id = ur.role_id " +
//						"JOIN users u ON u.id = ur.user_id " +
//						"WHERE u.username=?");
//
//		return manager;
//
//
//	}


	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http.authorizeHttpRequests( configurer ->
				configurer
						.requestMatchers("/css/**").permitAll()
						.requestMatchers("/", "/login", "/logout", "/access-denied", "/register-user", "/api/register-user" ).permitAll()
						.requestMatchers("/dashboard","/create-task", "/delete-tasks", "/update-task-status").authenticated()
						.requestMatchers("/api/**").hasAnyRole("USER", "ADMIN")
						.requestMatchers("/api/admin/**").hasRole("ADMIN")
						.anyRequest().authenticated()
		)
				.formLogin(form ->
						form
								.loginPage("/login")
								.loginProcessingUrl("/authenticateUser")
								.defaultSuccessUrl("/dashboard", true)
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

	@Bean
	public AuthenticationManager authManager(HttpSecurity http) throws Exception {
		AuthenticationManagerBuilder authenticationManagerBuilder =
				http.getSharedObject(AuthenticationManagerBuilder.class);
		authenticationManagerBuilder.authenticationProvider(daoAuthenticationProvider(customUserDetailsService));
		return authenticationManagerBuilder.build();
	}





}
