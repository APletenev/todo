package com.example.todo.controller;

import com.example.todo.model.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient(timeout = "36000")
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

}
