package com.example.todo.service;

import com.example.todo.model.Task;
import com.example.todo.repository.TagRepository;
import com.example.todo.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskServiceImpl implements TaskService {
    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Override
    public Task createChangeTask(Task task) {
        tagRepository.findById(task.getTask_tag().getTag_id()).orElseThrow(() -> new IllegalArgumentException("Не найден указанный в задаче тег"));
        return taskRepository.save(task);
    }

    @Override
    public void deleteTaskById(Long id) {
        taskRepository.deleteById(id);
    }

    @Override
    public Iterable<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @Override
    public Task getTaskById(Long id) {
        return taskRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Не найдена задача с id="+id));
    }
}
