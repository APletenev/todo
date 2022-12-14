package com.example.todo.controller;

import com.example.todo.model.Tag;
import com.example.todo.model.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient(timeout = "36000")
@ActiveProfiles("nosecurity_test")
public class TaskControllerTests {

    @Autowired
    private WebTestClient webTestClient;

    @Test
        // Проверяем, что нельзя создать задачу с несуществующим тегом
    void createTaskWithNotExistingTag() {
        Tag notExistingTag = new Tag(0L,"Несуществующий тег");
        Task task0 = new Task("Название задачи", "Задача с несуществующим тегом", LocalDate.now(), notExistingTag);
        webTestClient
                .post()
                .uri("/task")
                .bodyValue(task0)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Task.class)
                .consumeWith(System.out::println);
    }

    @Test
        // Проверяем, что нельзя создать задачу с пустым тегом
    void createTaskWithNullTag() {
        Task task0 = new Task("Название задачи", "Задача c пустым тегом", LocalDate.now(), null);
        webTestClient
                .post()
                .uri("/task")
                .bodyValue(task0)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Task.class)
                .consumeWith(System.out::println);
    }

    @Test
        //      Проверяем удаление несуществующей задачи
    void deleteNotExistingTask() {
        webTestClient
                .delete()
                .uri("/task/0")
                .exchange()
                .expectStatus().is4xxClientError()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .consumeWith(System.out::println);

        // TODO проверить ответ на некорректную строку запроса
    }

}
