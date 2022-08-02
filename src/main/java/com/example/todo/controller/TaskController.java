package com.example.todo.controller;

import com.example.todo.model.Task;
import com.example.todo.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    @PostMapping("/task") //добавить/изменить задачу
    public Task create(@RequestBody Task task) {
        return taskRepository.save(task);
    }

    @GetMapping("/tasks") //Вернуть список всех задач
    public Iterable<Task> get() {//получить все записи
        return taskRepository.findAll();
    }

    @DeleteMapping("/task/{id}") //удалить задачу  по id
    public void delete(@PathVariable Long id) {
        taskRepository.deleteById(id);
    }
}
