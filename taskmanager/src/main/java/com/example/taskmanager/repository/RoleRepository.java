package com.example.taskmanager.repository;

import com.example.taskmanager.entity.Role;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RoleRepository extends CrudRepository<Role, Integer> {

	@Query("SELECT r from Role r WHERE r.name = :name")
	Optional<Role> findByName(String name);


}
