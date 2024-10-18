package com.example.taskmanager.repository;

import com.example.taskmanager.entity.Role;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * RoleRepository is an interface for managing Role entities, providing
 * CRUD operations and custom queries to interact with the roles data source.
 */
public interface RoleRepository extends CrudRepository<Role, Integer> {

	/**
	 * Retrieves a Role entity from the database based on its name.
	 *
	 * @param name the name of the role to be retrieved.
	 * @return an Optional containing the Role if found, or an empty Optional if not found.
	 */
	@Query("SELECT r from Role r WHERE r.name = :name")
	Optional<Role> findByName(String name);


}
