package com.example.todo.repository;

import com.example.todo.model.Task;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface TaskRepository extends PagingAndSortingRepository<Task, Long> {
    @Query("SELECT t FROM Task t WHERE t.task_tag =:id")
    Iterable<Task> getTasksByTask_tag (@Param("id") Long id);
}