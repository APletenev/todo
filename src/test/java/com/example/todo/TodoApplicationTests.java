package com.example.todo;

import com.example.todo.model.Tag;
import com.example.todo.model.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient(timeout = "36000")
class TodoApplicationTests {
    @Autowired
    private WebTestClient webTestClient;

    Tag createTagTest(Tag tag) {
        Tag result = webTestClient
                .post()
                .uri("/tag")
                .bodyValue(tag)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Tag.class)
                .consumeWith(System.out::println)
                .returnResult().getResponseBody();

        assertEquals(tag.getTag_name(), result.getTag_name());
        return result;
    }

    Task createTaskTest(Task task) {
        Task result = webTestClient
                .post()
                .uri("/task")
                .bodyValue(task)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Task.class)
                .consumeWith(System.out::println)
                .returnResult().getResponseBody();

        assertEquals(task.getTask_name(), result.getTask_name());
        assertEquals(task.getTask_desc(), result.getTask_desc());
        assertEquals(task.getTask_date(), result.getTask_date());
        return result;

    }

    void checkTasksCount(int count) {
        webTestClient
                .get()
                .uri("/tasks")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Task.class)
                .hasSize(count)
                .consumeWith(System.out::println);
    }

    void checkNoTasks() {
        webTestClient
                .get()
                .uri("/tasks")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .json("[]")
                .consumeWith(System.out::println);
    }

    @Test
    void contextLoads() {
    }



    @Test
    void runIntegrationTests() throws Exception {

//      Проверяем, что имеем дело с пустой базой и контроллер возвращает пустой список задач
        checkNoTasks();


        // Создаем тег1
        Tag tag1 = new Tag("home");
        tag1 = createTagTest(tag1);

        // Создаем тег2
        Tag tag2 = new Tag("work");
        tag2 = createTagTest(tag2);

        // Проверить, что нельзя создать задачу без имени
        Task task0 = new Task("", "Задача без имени", LocalDate.now(), tag1.getTag_id());
        webTestClient
                .post()
                .uri("/task")
                .bodyValue(task0)
                .exchange()
                .expectStatus().isBadRequest()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Task.class)
                .consumeWith(System.out::println);

        // Создаем задачу 1 с привязкой к тегу 1
        Task task1 = new Task("Задача 1", "Описание задачи 1", LocalDate.now(), tag1.getTag_id());
        task1 = createTaskTest(task1);

        // Создаем задачу 2 с привязкой к тегу 2
        Task task2 = new Task("Задача 2", "Описание задачи 2", LocalDate.now(), tag2.getTag_id());
        task2 = createTaskTest(task2);

        // Создаем задачу 3 с привязкой к тегу 2
        Task task3 = new Task("Задача 3", "Описание задачи 3", LocalDate.now(), tag2.getTag_id());
        task3 = createTaskTest(task3);

//      Проверяем, что контроллер возвращает список из 3 задач
        checkTasksCount(3);

//      Изменяем имя второй задачи
        task2.setTask_name("Новая задача 2");
        Task newtask2 = createTaskTest(task2);

//      Проверяем что имя второй задачи изменилось
        assertEquals(task2.getTask_name(), newtask2.getTask_name());

//      Проверяем что ID второй задачи не изменился
        assertEquals(task2.getTask_id(), newtask2.getTask_id());

//      Проверяем, что количество задач не изменилось
        checkTasksCount(3);

//      Меняем название тега2
        tag2.setTag_name("business");
        Tag newtag2 = createTagTest(tag2);

//      Проверяем что ID второго тега не изменился
        assertEquals(tag2.getTag_id(), newtag2.getTag_id());


//      Получаем тег2 по УИД и все его задачи
        tag2 = webTestClient
                .get()
                .uri("/tag/"+newtag2.getTag_id())
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Tag.class)
                .consumeWith(System.out::println)
                .returnResult().getResponseBody();

//      Проверяем, что у тега2 есть 2 задачи
        assertEquals(tag2.getTasks().size(), 2);

//      Проверяем задачи тега
        assertEquals(tag2.getTasks().get(0), newtask2);
        assertEquals(tag2.getTasks().get(1), task3);


//      Проверяем возможность каскадно удалить тег со всеми прикрепленными к нему задачами
        webTestClient
                .delete()
                .uri("/tag/"+newtag2.getTag_id())
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .consumeWith(System.out::println);

//      Убеждаемся, что осталась только одна задача
        checkTasksCount(1);

//      Удаляем задачу по УИД
        webTestClient
                .delete()
                .uri("/task/"+task1.getTask_id())
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .consumeWith(System.out::println);

//      Проверяем, что больше нет ни одной задачи
        checkNoTasks();

    }


}
