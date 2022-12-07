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
        when(tagRepository.findById(eq(1L)))
                .thenReturn(Optional.of(new Tag("cached")));
        Tag tag1 = tagService.getTagById(1L);
        Tag tag2 = tagService.getTagById(1L);

        verify(tagRepository,times(1)).findById(Mockito.any());
    }



}
