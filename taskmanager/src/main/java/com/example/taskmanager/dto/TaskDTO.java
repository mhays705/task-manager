package com.example.taskmanager.dto;


import com.example.taskmanager.entity.Task;
import com.example.taskmanager.entity.User;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Data Transfer Object (DTO) representing a task in the system.
 *
 * The TaskDTO class is used to encapsulate data for task operations,
 * including creation, reading, and updating tasks.
 *
 * Fields:
 * - id: A unique identifier for the task.
 * - taskName: The name of the task, which must not be blank and should be 255 characters or less.
 * - startDate: The date when the task starts or started. It must not be blank and must be in the past or present.
 * - dueDate: The date by which the task should be completed. It must be a future date.
 * - user: The user associated with the task.
 * - taskStatus: The status of the task, indicating whether it is completed.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskDTO {


	private int id;

	@NotBlank(message = "is required")
	@Size(max = 255, message = "Name must be 255 characters or less")
	private String taskName;

	@NotBlank(message = "Start date is required")
	@PastOrPresent(message = "Start date must be current day or earlier")
	private LocalDate startDate;

	@Future(message = "Due date must be later date")
	private LocalDate dueDate;

	private User user;

	private boolean taskStatus;

}
