package com.example.todo.controller;

import com.example.todo.model.Task;
import com.example.todo.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    @PostMapping("/task") //создать запись
    public Task create(@RequestBody Task task) {
        return taskRepository.save(task);
    }

    @GetMapping("/tasks") //получить список всех задач
    public Iterable<Task> get() {//получить все записи
        return taskRepository.findAll();
    }
}
