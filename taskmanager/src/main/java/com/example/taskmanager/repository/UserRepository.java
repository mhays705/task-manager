package com.example.taskmanager.repository;

import com.example.taskmanager.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Integer> {

	@Query("SELECT u FROM User u WHERE u.username = :username")
	User findByUsername(String username);



	@Query("SELECT u FROM User u WHERE u.id = :id")
	User findUserById(int id);

	boolean existsByEmail(String email);

	boolean existsByUsername(String username);

	boolean existsById(int id);



}


