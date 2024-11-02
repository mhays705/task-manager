package com.example.taskmanager.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a role entity in the system.
 *
 * The Role entity is used to define specific roles or permissions
 * that can be assigned to users. Roles are used for access control,
 * determining what actions a user is allowed to perform in the application.
 *
 * Roles are identified uniquely by their ID and have a name attribute to
 * specify the type of role, such as "ADMIN", "USER", etc.
 *
 * This entity is managed by JPA and mapped to a database table named "roles".
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "roles")
public class Role {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Column(name = "name")
	private String name;


	/**
	 * Extracts and formats the display name from the role's name attribute.
	 *
	 * The method assumes that the role name contains a prefix of at least five characters
	 * which should be removed. It converts the remaining part of the name to lowercase
	 * and then capitalizes the first letter.
	 *
	 * @return the formatted display name derived from the role name.
	 */
	public String getDisplayName() {
		String displayName = name.substring(5);
		displayName = displayName.toLowerCase();
		return Character.toUpperCase(displayName.charAt(0)) + displayName.substring(1);
	}

}


