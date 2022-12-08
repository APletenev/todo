package com.example.todo.controller;

import com.example.todo.model.Tag;
import com.example.todo.repository.TagRepository;
import com.example.todo.service.TagServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient(timeout = "36000")
@ActiveProfiles("nosecurity_test")
@DirtiesContext // Для того, чтобы остальные классы тестов не использовали данный InjectMocks
public class TagControllerTests {

    @Autowired
    private WebTestClient webTestClient;

    @Mock
    private TagRepository tagRepository;

    @Autowired
    @InjectMocks
    private TagServiceImpl tagService;


    @Test
        // Проверяем, что нельзя создать пустой тег
    void createEmptyTag() {
        Tag tag0 = new Tag();
        webTestClient
                .post()
                .uri("/tag")
                .bodyValue(tag0)
                .exchange()
                .expectStatus().isBadRequest()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Tag.class)
                .consumeWith(System.out::println);

    }

    @Test
        //      Проверяем, что не работает получение тега с несущестуующим УИД
    void getNotExistingTag() {
        webTestClient
                .get()
                .uri("/tag/0")
                .exchange()
                .expectStatus().is4xxClientError()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .consumeWith(System.out::println);
    }

    @Test
        //      Проверяем, что работает кэширование запроса Get tag by ID
    void GetTagCachingTest() {

        when(tagRepository.findById(Mockito.any()))
                .thenAnswer(i -> Optional.of(new Tag(1L, "cached")));
        when(tagRepository.save(Mockito.any()))
                .then(returnsFirstArg());

        Tag tagFromService = tagService.getTagById(1L);
        Tag tagFromCache = tagService.getTagById(1L);

        //Запрос вызывался только один раз
        verify(tagRepository, times(1)).findById(Mockito.any());

        tagService.deleteTagById(1L); //Очищается кэш

        Tag tagBeforeUpdate = tagService.getTagById(1L);

        //Запрос вызван второй раз
        verify(tagRepository, times(2)).findById(Mockito.any());

        Tag tagUpdated = tagService.createChangeTag(new Tag(1L, "updated"));

        //Запрос вызван третий раз (внутри createChangeTag)
        verify(tagRepository, times(3)).findById(Mockito.any());

        Tag tagCached = tagService.getTagById(1L);

        //Запрос больше не вызывался
        verify(tagRepository, times(3)).findById(Mockito.any());

        // Из кэша был получен обновленный тег
        assertEquals(tagUpdated, tagCached);

    }


}
