package com.example.todo.service;

import com.example.todo.model.Task;

public interface TaskService {
    Task createChangeTask (Task task);
    void deleteTaskById (Long id);

    Iterable<Task> getAllTasks ();

    Task getTaskById(Long id);
}
