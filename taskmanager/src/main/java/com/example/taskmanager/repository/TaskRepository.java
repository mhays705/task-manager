package com.example.taskmanager.repository;

import com.example.taskmanager.dto.TaskDTO;
import com.example.taskmanager.entity.Task;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TaskRepository extends CrudRepository<Task, Integer> {

    @Query("SELECT t from Task t where t.startDate = :startDate")
    Optional<Task> findByStartDate(LocalDate startDate);

    @Query("SELECT t from Task t where t.dueDate = :dueDate")
    Optional<Task> findByDueDate(LocalDate dueDate);

    @Query("SELECT t FROM Task t WHERE t.user.id = :userId")
    List<Task> findTasksByUserId(@Param("userId") int id);

}
