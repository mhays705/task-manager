package com.example.taskmanager.repository;

import com.example.taskmanager.entity.Task;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface TaskRepository extends CrudRepository<Task, Integer> {

    @Query("SELECT t from Task t where t.startDate = :startDate")
    Optional<Task> findByStartDate(LocalDate startDate);

    @Query("SELECT t from Task t where t.dueDate = :dueDate")
    Optional<Task> findByDueDate(LocalDate dueDate);

}
