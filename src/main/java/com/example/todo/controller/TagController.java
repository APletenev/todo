package com.example.todo.controller;


import com.example.todo.model.Tag;
import com.example.todo.model.Task;
import com.example.todo.repository.TagRepository;
import com.example.todo.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TagController {

    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private TaskRepository taskRepository;

    @PostMapping("/tag") //добавить/изменить тег
    public Tag create(@RequestBody Tag tag) {
        return tagRepository.save(tag);
    }

    @DeleteMapping("/tag/{id}") //удалить тег со всеми прикрепленными к нему задачами
    public void delete(@PathVariable Long id) {
        tagRepository.deleteById(id);
    }

    @GetMapping("/tag/{id}") //получить один тег по УИД и все его задачи
    public Tag byTagId(@PathVariable Long id) {
        if (!tagRepository.existsById(id)) return null;
        Tag tag = tagRepository.findById(id).get();
        tag.setTasks((List<Task>) taskRepository.getTasksByTask_tag(id));
        return tag;
    }

}
