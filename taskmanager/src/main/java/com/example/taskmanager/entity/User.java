package com.example.taskmanager.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

/**
 * Represents a user entity in the system.
 *
 * The User entity holds essential details about a user, including their username, first name, last name,
 * email, password, and roles. This entity is managed by JPA and mapped to a database table named "users".
 *
 * Fields:
 * - id: Unique identifier for the user.
 * - username: Unique username of the user. It must be between 2 to 45 characters.
 * - firstName: First name of the user. It must be between 1 to 45 characters.
 * - lastName: Last name of the user. It must be between 1 to 45 characters.
 * - email: Unique email address of the user. It must be a valid email and less than 255 characters.
 * - password: Password of the user. It must be at least 5 characters.
 * - enabled: Indicates whether the user is enabled. Defaults to true.
 * - tasks: List of tasks associated with the user. It establishes a one-to-many relationship with the Task entity.
 * - roles: Set of roles assigned to the user. It establishes a many-to-many relationship with the Role entity.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;


	@NotBlank(message = "is required")
	@Size(min = 2, max = 45, message = "Username must be 5 - 45 characters")
	@Column(name = "username", unique = true)
	private String username;

	@NotBlank(message = "is required")
	@Size(min = 1, max = 45, message = "First name must be 1 - 45 characters")
	@Column(name = "first_name")
	private String firstName;

	@NotBlank(message = "is required")
	@Size(min = 1, max = 45, message = "Last name must be 1 - 45 characters")
	@Column(name = "last_name")
	private String lastName;

	@NotBlank(message = "is required")
	@Email(message = "Enter valid email address")
	@Size(max = 255, message = "Must be less than 255 characters")
	@Column(name = "email", unique = true)
	private String email;

	@NotBlank(message = "is required")
	@Size(min = 5, message = "password must be at least 5 characters ")
	@Column(name = "password")
	private String password;

	@Column(name = "enabled")
	private boolean enabled = true;

	@OneToMany( mappedBy = "user", cascade = {CascadeType.DETACH, CascadeType.MERGE,
			CascadeType.REFRESH, CascadeType.PERSIST, CascadeType.REMOVE} )
	private List<Task> tasks;

	@ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.MERGE,
				CascadeType.REFRESH, CascadeType.PERSIST})
	@JoinTable(
			name = "user_roles",
			joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "role_id")
	)
	private Set<Role> roles;




}
