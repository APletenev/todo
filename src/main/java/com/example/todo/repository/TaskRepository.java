package com.example.todo.repository;

import com.example.todo.model.Task;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface TaskRepository extends PagingAndSortingRepository<Task, Long> {

}