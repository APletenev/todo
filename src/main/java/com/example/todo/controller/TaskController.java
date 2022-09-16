package com.example.todo.controller;

import com.example.todo.model.Task;
import com.example.todo.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    /**
     * Если задачи с таким же id не существует, создает задачу. Если задача с таким id уже существует, изменяет ее в соответствии с переданными значениями
     * @param task Задача, которую необходимо создать или изменить
     * @return Задача, которая сохранена в репозиторий
     */
    @PostMapping("/task")
    public Task create(@RequestBody Task task) {
        return taskRepository.save(task);
    }

    /**
     * Возвращает список всех задач
     * @return Список задач
     */
    @GetMapping("/tasks")
    public Iterable<Task> get() {//получить все записи
        return taskRepository.findAll();
    }

    /** Удаляет задачу  по id
     * @param id УИД задачи, которую требуется удалить
     */
    @DeleteMapping("/task/{id}")
    public void delete(@PathVariable Long id) {
        taskRepository.deleteById(id);
    }
}
