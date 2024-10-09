package com.example.taskmanager.service;

import com.example.taskmanager.entity.Role;
import com.example.taskmanager.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

	private final RoleRepository roleRepository;

	@Autowired
	public RoleServiceImpl(RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}

	@Override
	public Role getUserRole() {
		return roleRepository.findByName("ROLE_USER")
				.orElseThrow(() -> new RuntimeException("Default USER role not found in database."));
	}
}
