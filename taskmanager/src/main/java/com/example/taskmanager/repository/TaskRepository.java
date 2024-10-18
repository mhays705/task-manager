package com.example.taskmanager.repository;

import com.example.taskmanager.dto.TaskDTO;
import com.example.taskmanager.entity.Task;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing Task entities.
 * Extends the CrudRepository interface to provide basic CRUD operations.
 */
public interface TaskRepository extends CrudRepository<Task, Integer> {

    /**
     * Finds a Task entity based on its start date.
     *
     * @param startDate the start date of the task to be retrieved
     * @return an Optional containing the Task if found, otherwise an empty Optional
     */
    @Query("SELECT t from Task t where t.startDate = :startDate")
    Optional<Task> findByStartDate(LocalDate startDate);

    /**
     * Finds a task by its due date.
     *
     * @param dueDate the due date of the task to be retrieved
     * @return an Optional containing the Task if found, otherwise an empty Optional
     */
    @Query("SELECT t from Task t where t.dueDate = :dueDate")
    Optional<Task> findByDueDate(LocalDate dueDate);

    /**
     * Retrieves tasks associated with a specific user based on the user ID.
     *
     * @param id the ID of the user whose tasks are to be fetched
     * @return a list of tasks associated with the specified user ID
     */
    @Query("SELECT t FROM Task t WHERE t.user.id = :userId")
    List<Task> findTasksByUserId(@Param("userId") int id);

}
