package com.example.todo.controller;


import com.example.todo.model.Tag;
import com.example.todo.repository.TagRepository;
import com.example.todo.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class TagController {

    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private TaskRepository taskRepository;

    /**добавить/изменить тег
     * Создает тег
     * @param tag тег, который необходимо создать
     * @return созданный тег
     */
    @PostMapping("/tag")
    public Tag create(@RequestBody Tag tag) {
        return tagRepository.save(tag);
    }

    /**
     * Удаляет тег со всеми прикрепленными к нему задачами
     * @param id  ид тега для удаления
     */
    @DeleteMapping("/tag/{id}")
    public void delete(@PathVariable Long id) {
        tagRepository.deleteById(id);
    }

    /**
     * Получает один тег по УИД и все его задачи
     * @param id УИД искомого тега
     * @return Найденный тег
     * @exception IllegalArgumentException В случае, если тег с таким УИД не найден в базе
     */
    @GetMapping("/tag/{id}")
    public Tag byTagId(@PathVariable Long id) {
        return tagRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("tag not found"));
    }


}
