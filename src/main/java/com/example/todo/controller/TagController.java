package com.example.todo.controller;


import com.example.todo.model.Tag;
import com.example.todo.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class TagController {

    @Autowired
    private TagRepository tagRepository;

    /**
     * добавить/изменить тег
     * Если уже существует тег, с указанным ID, то меняется его имя на указанное
     * В противном случае, создается новый тег
     *
     * @param tag тег, который необходимо создать
     * @return созданный тег
     */
    @PostMapping("/tag")
    public Tag create(@RequestBody @Valid Tag tag) {
        if (tag.getTag_id() != null) { // Изменяем существующий тег
            Tag t = tagRepository.findById(tag.getTag_id()).orElse(tag);
            if (!t.equals(tag)) t.setTag_name(tag.getTag_name());
            return tagRepository.save(t);
        } else return tagRepository.save(tag); // Создаем новый тег
    }

    /**
     * Удаляет тег со всеми прикрепленными к нему задачами
     *
     * @param id ид тега для удаления
     */
    @DeleteMapping("/tag/{id}")
    public void delete(@PathVariable Long id) {
        tagRepository.deleteById(id);
    }

    /**
     * Получает один тег по УИД и все его задачи
     *
     * @param id УИД искомого тега
     * @return Найденный тег
     * @throws IllegalArgumentException В случае, если тег с таким УИД не найден в базе
     */
    @GetMapping("/tag/{id}")
    public Tag byTagId(@PathVariable Long id) {
        return tagRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Тег не найден"));
    }


}
