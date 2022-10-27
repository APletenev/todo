package com.example.todo.service;

import com.example.todo.model.Tag;

public interface TagService {

    Tag createChangeTag (Tag tag);
    void deleteTagById (Long id);

    Tag getTagById (Long id);
}
