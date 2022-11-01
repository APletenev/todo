package com.example.todo.security;

import com.example.todo.TodoApplication;
import com.example.todo.model.Tag;
import com.example.todo.model.Task;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static com.example.todo.security.SecurityConfig.EditorRole;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TodoApplication.class)
@WebAppConfiguration
@ActiveProfiles("security_test")
class SecurityTests {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper mapper;

    private MockMvc mvc;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    void notAuthenticatedGetTasksShouldOk() throws Exception {
        mvc
                .perform(get("/tasks")
                        .secure(true))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }

    @Test
    void notAuthenticatedGetTagShould404() throws Exception {
        mvc
                .perform(get("/tag/0")
                        .secure(true))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound());
    }

    @Test
    void notHttpsGetTasksShould302() throws Exception {
        mvc
                .perform(get("/tasks"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isFound());
    }

    @Test
    void notAuthenticatedPostTagShould401() throws Exception {
        Tag tag = new Tag("test");
        mvc
                .perform(post("/tag")
                        .secure(true)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(tag))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = EditorRole)
    void authenticatedPostTagShouldOk() throws Exception {
        Tag tag = new Tag("test");
        mvc
                .perform(post("/tag")
                        .secure(true)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(tag))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }

    @Test
    void wrongUserPostTagShould401() throws Exception {
        Tag tag = new Tag("test");
        mvc
                .perform(post("/tag")
                        .secure(true)
                        .with(httpBasic("not_existing_user", "not_existing_password"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(tag))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    void notAuthenticatedPostTaskShould401() throws Exception {
        Task task = new Task();
        mvc
                .perform(post("/task")
                        .secure(true)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(task))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = EditorRole)
    void authenticatedPostTaskShould400() throws Exception {
        Task task = new Task();
        mvc
                .perform(post("/task")
                        .secure(true)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(task))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void notAuthenticatedDeleteTagShould401() throws Exception {
        mvc
                .perform(delete("/tag/0")
                        .secure(true))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = EditorRole)
    void authenticatedDeleteTagShould404() throws Exception {
        mvc
                .perform(delete("/tag/0")
                        .secure(true))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound());
    }

}