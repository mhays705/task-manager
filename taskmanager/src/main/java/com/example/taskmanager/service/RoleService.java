package com.example.taskmanager.service;

import com.example.taskmanager.entity.Role;
import com.example.taskmanager.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;


public interface RoleService {

	public Role getUserRole();

	public List<Role> findAll();

	public Role findByID(int id);


}
