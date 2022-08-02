package com.example.todo.controller;

import com.example.todo.model.Tag;
import com.example.todo.model.Task;
import com.example.todo.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


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

    @GetMapping("/tag/{id}") //получить запись по id
    public Iterable<Task> byTagId(@PathVariable Long id) {
        return taskRepository.getTasksByTask_tag(id);
    }

    @DeleteMapping("/task/{id}") //удалить запись по id
    public void delete(@PathVariable Long id) {
        taskRepository.deleteById(id);
    }
}
