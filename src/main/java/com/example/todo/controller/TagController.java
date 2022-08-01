package com.example.todo.controller;


import com.example.todo.model.Tag;
import com.example.todo.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class TagController {

    @Autowired
    private TagRepository tagRepository;

    @PostMapping("/tag") //создать запись
    public Tag create(@RequestBody Tag tag) {
        return tagRepository.save(tag);
    }

}
