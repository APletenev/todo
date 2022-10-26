package com.example.todo.controller;

import com.example.todo.model.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient(timeout = "36000")
@ActiveProfiles("nosecurity_test")
public class TagControllerTests {

    @Autowired
    private WebTestClient webTestClient;

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

}
