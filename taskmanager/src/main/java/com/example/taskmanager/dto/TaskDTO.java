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
