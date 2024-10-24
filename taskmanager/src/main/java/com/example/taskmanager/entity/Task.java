package com.example.taskmanager.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Represents a task entity in the system.
 *
 * The Task entity holds relevant details about a task, including its name, start date, due date, status,
 * and the user to whom the task is assigned. This entity is managed by JPA and mapped to a database table named "task".
 *
 * Fields:
 * - id: Unique identifier for the task.
 * - taskName: Name of the task. It must not be blank and should be 255 characters or less.
 * - startDate: The date when the task started or will start. It must not be null and can be a past or present date.
 * - dueDate: The date by which the task should be completed. It must be a future date.
 * - taskStatus: Status of the task, indicating whether it is completed.
 * - user: The user to whom the task is assigned. This field establishes a many-to-one relationship with the User entity.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "task")
public class Task {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@NotBlank(message = "is required")
	@Size(max = 255, message = "Name must be 255 characters or less")
	@Column(name = "task_name")
	private String taskName;

	@NotNull(message = "Start date is required")
	@PastOrPresent(message = "Start date must be current day or earlier")
	@Column(name = "start_date")
	private LocalDate startDate;

	@Future(message = "Due date must be in the future")
	@Column(name = "due_date")
	private LocalDate dueDate;

	@Column(name = "task_status")
	private boolean taskStatus;


	@ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.MERGE,
			CascadeType.PERSIST, CascadeType.REFRESH})
	@JoinColumn(name = "user_id")
	private User user;




}



