package com.example.taskmanager.dto;

import com.example.taskmanager.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Data Transfer Object for a web-based task.
 *
 * This class is used to transfer task-related data
 * between the web layer and the service layer.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WebTaskDTO {


	private int id;

	@NotBlank(message = "Task name is required")
	@Size(max = 255, message = "Name must be 255 characters or less")
	private String taskName;

	@NotNull(message = "Start date is required")
	@PastOrPresent(message = "Start date must be current day or earlier")
	private LocalDate startDate;

	@Future(message = "Due date must be in future")
	private LocalDate dueDate;

	private boolean taskStatus;

	private int userId;









}
