package com.example.todo.repository;

import com.example.todo.model.Tag;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface TagRepository extends PagingAndSortingRepository<Tag, Long> {

}
