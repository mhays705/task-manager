package com.example.taskmanager.repository;

import com.example.taskmanager.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

/**
 * UserRepository is an interface for managing User entities in the database.
 * This interface extends the CrudRepository interface provided by Spring Data JPA,
 * offering standard CRUD operations as well as custom queries.
 */
public interface UserRepository extends CrudRepository<User, Integer> {

	/**
	 * Finds a User entity by its username.
	 *
	 * @param username the username to search for
	 * @return the User entity associated with the given username,
	 *         or null if no User entity is found with that username
	 */
	@Query("SELECT u FROM User u WHERE u.username = :username")
	User findByUsername(String username);



	/**
	 * Retrieves a User entity from the database using the provided user ID.
	 *
	 * @param id the unique identifier of the User to be retrieved.
	 * @return the User entity that matches the given ID, or null if no such User exists.
	 */
	@Query("SELECT u FROM User u WHERE u.id = :id")
	User findUserById(int id);

	/**
	 * Checks if a user with the given email exists in the repository.
	 *
	 * @param email the email to check for existence
	 * @return true if a user with the given email exists, false otherwise
	 */
	boolean existsByEmail(String email);

	/**
	 * Checks if a user with the given username exists in the database.
	 *
	 * @param username the username to check for existence
	 * @return true if a user with the given username exists, false otherwise
	 */
	boolean existsByUsername(String username);

	/**
	 * Checks if an entity with the given ID exists in the database.
	 *
	 * @param id the unique identifier of the entity to check
	 * @return true if an entity with the given ID exists, false otherwise
	 */
	boolean existsById(int id);



}


