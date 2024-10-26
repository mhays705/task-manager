package com.example.taskmanager.service;

import com.example.taskmanager.entity.Role;
import com.example.taskmanager.repository.RoleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of the RoleService interface responsible for handling
 * operations related to user roles.
 *
 * This service interacts with the RoleRepository to fetch role information
 * from the database.
 */
@Service
public class RoleServiceImpl implements RoleService {

	private final RoleRepository roleRepository;

	/**
	 * Constructs a new RoleServiceImpl with the specified RoleRepository.
	 *
	 * @param roleRepository the RoleRepository used to interact with the roles data source
	 */
	@Autowired
	public RoleServiceImpl(RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}

	/**
	 * Fetches the role of a user from the database.
	 *
	 * @return the Role object associated with a user, specifically the role named "ROLE_USER".
	 * @throws RuntimeException if the "ROLE_USER" role is not found in the database.
	 */
	@Override
	public Role getUserRole() {
		return roleRepository.findByName("ROLE_USER")
				.orElseThrow(() -> new RuntimeException("Default USER role not found in database."));
	}

	/**
	 * Retrieves all roles from the repository.
	 *
	 * @return a list of all roles available in the repository
	 */
	@Override
	public List<Role> findAll() {
		List<Role> roles = new ArrayList<>();
		for (Role role : roleRepository.findAll()) {
			roles.add(role);
		}
		return roles;
	}


	/**
	 * Retrieves a Role entity from the repository based on its ID.
	 *
	 * @param id the unique identifier of the Role to be retrieved
	 * @return the Role object associated with the specified ID
	 * @throws EntityNotFoundException if no Role is found with the specified ID
	 */
	@Override
	public Role findByID(int id) {

		return roleRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException());
	}
}

