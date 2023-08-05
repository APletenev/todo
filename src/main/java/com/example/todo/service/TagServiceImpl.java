package com.example.todo.service;

import com.example.todo.model.Tag;
import com.example.todo.repository.TagRepository;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@Setter // Для работы @InjectMocks
public class TagServiceImpl implements TagService {

    @Autowired
    private TagRepository tagRepository;

    @CachePut(value = "tag", key = "#tag.tag_id")
    @Override
    public Tag createChangeTag(Tag tag) {
        return tagRepository.save(tag);
    }

    @CacheEvict(value = "tag", key = "#id")
    @Override
    public void deleteTagById(Long id) {
        tagRepository.deleteById(id);
    }

    @Cacheable(value = "tag", key = "#id")
    @Override
    public Tag getTagById(Long id) {
        return tagRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Тег не найден"));
    }
}
