package com.collab.user.controller;

import com.collab.user.model.User;
import com.collab.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void register_returnsSavedUser() throws Exception {
        User input = new User();
        input.setUsername("bob");
        input.setPassword("pw");
        input.setEmail("b@example.com");

        when(service.register(any(User.class))).thenReturn(input);

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("bob"));
    }

    @Test
    void login_success_returnsUser() throws Exception {
        User user = new User();
        user.setUsername("alice");
        when(service.login("alice", "secret")).thenReturn(Optional.of(user));

        mockMvc.perform(post("/api/users/login")
                        .param("username", "alice")
                        .param("password", "secret"))
                .andExpect(status().isOk());
    }

    @Test
    void login_failure_returns401() throws Exception {
        when(service.login(eq("alice"), eq("bad"))).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/users/login")
                        .param("username", "alice")
                        .param("password", "bad"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void update_returnsUpdatedUser() throws Exception {
        User patch = new User();
        patch.setEmail("new@example.com");
        patch.setPassword("npw");

        User updated = new User();
        updated.setUsername("alice");
        updated.setEmail("new@example.com");

        when(service.updateProfile(eq(1L), any(User.class))).thenReturn(updated);

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patch)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("new@example.com"));
    }
}
