package com.example.todo;

import com.example.todo.model.Tag;
import com.example.todo.model.Task;
import com.example.todo.service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest (webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient(timeout = "36000")
@ActiveProfiles("nosecurity_test")
class TodoApplicationTests {

    public static final String RESET = "\033[0m";

    public static final String BLUE = "\033[0;34m";

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    TaskService taskService;

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

        System.out.println(BLUE + "Проверяем, что имеем дело с пустой базой и контроллер возвращает пустой список задач"+RESET);
        checkNoTasks();

        System.out.println(BLUE + "Создаем тег1"+RESET);

        Tag tag1 = new Tag("home");
        tag1 = createTagTest(tag1);

        System.out.println(BLUE + "Создаем тег2"+RESET);
        Tag tag2 = new Tag("work");
        tag2 = createTagTest(tag2);

        System.out.println(BLUE + "Проверить, что нельзя создать задачу без имени"+RESET);
        Task task0 = new Task("", "Задача без имени", LocalDate.now(), tag1);
        webTestClient
                .post()
                .uri("/task")
                .bodyValue(task0)
                .exchange()
                .expectStatus().isBadRequest()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Task.class)
                .consumeWith(System.out::println);

        System.out.println(BLUE + "Создаем задачу 1 с привязкой к тегу 1"+RESET);
        Task task1 = new Task("Задача 1", "Описание задачи 1", LocalDate.now(), tag1);
        task1 = createTaskTest(task1);

        System.out.println(BLUE + "Создаем задачу 2 с привязкой к тегу 2"+RESET);
        Task task2 = new Task("Задача 2", "Описание задачи 2", LocalDate.now(), tag2);
        task2 = createTaskTest(task2);

        System.out.println(BLUE + "Создаем задачу 3 с привязкой к тегу 2"+RESET);
        Task task3 = new Task("Задача 3", "Описание задачи 3", LocalDate.now(), tag2);
        task3 = createTaskTest(task3);

        System.out.println(BLUE + "Проверяем, что контроллер возвращает список из 3 задач"+RESET);
        checkTasksCount(3);

        System.out.println(BLUE + "Изменяем имя второй задачи"+RESET);
        task2.setTask_name("Новая задача 2");
        Task newtask2 = createTaskTest(task2);

        System.out.println(BLUE + "Проверяем что имя второй задачи изменилось"+RESET);
        assertEquals(task2.getTask_name(), newtask2.getTask_name());

        System.out.println(BLUE + "Проверяем что ID второй задачи не изменился"+RESET);
        assertEquals(task2.getTask_id(), newtask2.getTask_id());

        System.out.println(BLUE + "Проверяем, что количество задач не изменилось"+RESET);
        checkTasksCount(3);

        System.out.println(BLUE + "Меняем название тега2"+RESET);
        tag2.setTag_name("business");
        Tag newtag2 = createTagTest(tag2);

        System.out.println(BLUE + "Проверяем что ID второго тега не изменился"+RESET);
        assertEquals(tag2.getTag_id(), newtag2.getTag_id());

        System.out.println(BLUE + "Получаем тег2 по УИД и все его задачи"+RESET);
        tag2 = webTestClient
                .get()
                .uri("/tag/" + newtag2.getTag_id())
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Tag.class)
                .consumeWith(System.out::println)
                .returnResult().getResponseBody();

        System.out.println(BLUE + "Проверяем, что у тега2 есть 2 задачи"+RESET);
        assertEquals(tag2.getTasks().size(), 2);

        System.out.println(BLUE + "Проверяем задачи тега"+RESET);

        newtask2 = taskService.getTaskById(newtask2.getTask_id());
        task3 = taskService.getTaskById(task3.getTask_id());
        assertTrue(tag2.getTasks().contains(task3));
        assertTrue(tag2.getTasks().contains(newtask2));

        System.out.println(BLUE + "Проверяем возможность каскадно удалить тег со всеми прикрепленными к нему задачами"+RESET);
        webTestClient
                .delete()
                .uri("/tag/" + newtag2.getTag_id())
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .consumeWith(System.out::println);

        System.out.println(BLUE + "Убеждаемся, что осталась только одна задача"+RESET);
        checkTasksCount(1);

        System.out.println(BLUE + "Удаляем задачу по УИД"+RESET);
        webTestClient
                .delete()
                .uri("/task/" + task1.getTask_id())
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .consumeWith(System.out::println);

        System.out.println(BLUE + "Проверяем, что больше нет ни одной задачи"+RESET);
        checkNoTasks();

    }

}
