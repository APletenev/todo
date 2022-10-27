package com.example.todo.controller;

import com.example.todo.Marker;
import com.example.todo.model.Task;
import com.example.todo.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@Validated
public class TaskController {

    @Autowired
    private TaskService taskService;

    /**
     * Если задачи с таким же id не существует, создает задачу. Если задача с таким id уже существует, изменяет ее в соответствии с переданными значениями
     * @param task Задача, которую необходимо создать или изменить
     * @return Задача, которая сохранена в репозиторий
     */
    @PostMapping("/task")
    @Validated({Marker.OnCreate.class})
    public Task create(@RequestBody @Valid Task task) {
        return taskService.createChangeTask(task);
    }

    /**
     * Возвращает список всех задач
     * @return Список задач
     */
    @GetMapping("/tasks")
    public Iterable<Task> get() {//получить все записи
        return taskService.getAllTasks();
    }

    /** Удаляет задачу  по id
     * @param id УИД задачи, которую требуется удалить
     */
    @DeleteMapping("/task/{id}")
    public void delete(@PathVariable Long id) {
        taskService.deleteTaskById(id);
    }
}
