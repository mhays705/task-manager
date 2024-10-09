package com.example.taskmanager.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

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

	@Future(message = "Due date must be n")
	@Column(name = "due_date")
	private LocalDate dueDate;

	@Column(name = "task_status")
	private boolean taskStatus;


	@ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.MERGE,
			CascadeType.PERSIST, CascadeType.REFRESH})
	@JoinColumn(name = "user_id")
	private User user;




}



