package com.example.taskmanager.service;

import com.example.taskmanager.entity.Role;
import com.example.taskmanager.entity.User;
import com.example.taskmanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Service to load user-specific data.
 *
 * This service is an implementation of the {@link UserDetailsService} which is used by Spring Security.
 * It retrieves user details from the database based on the provided username.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;

	/**
	 * Constructor to initialize CustomUserDetailsService with UserRepository.
	 *
	 * @param userRepository the repository used to interact with the user data source
	 */
	@Autowired
	public CustomUserDetailsService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	/**
	 * Loads the user from the database based on the provided username.
	 *
	 * @param username the username identifying the user whose data is required.
	 * @return UserDetails a fully populated user record with username, password, and authorities.
	 * @throws UsernameNotFoundException if the user could not be found or the user has no granted authority.
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username);

		if (user == null) {
			throw new RuntimeException("User with username : " + username + " not found");
		}


		Collection<SimpleGrantedAuthority> authorities = mapRolesToAuthorities(user.getRoles());


		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
				authorities);
	}

	/**
	 * Converts a collection of Role objects to a collection of SimpleGrantedAuthority objects.
	 *
	 * @param roles the collection of Role objects to be converted
	 * @return a collection of SimpleGrantedAuthority objects derived from the roles
	 */
	private Collection<SimpleGrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
		return roles.stream()
				.map(role -> new SimpleGrantedAuthority(role.getName()))
				.collect(Collectors.toList());
	}







}
