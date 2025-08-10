package com.collab.doc.controller;

import com.collab.doc.model.Document;
import com.collab.doc.service.DocumentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DocCompatibilityController.class)
class DocCompatibilityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DocumentService documentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void create_shouldReturnOk() throws Exception {
        Document d = new Document();
        d.setTitle("T");
        when(documentService.create(any(Document.class))).thenReturn(d);
        mockMvc.perform(post("/api/docs/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(d)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("T"));
    }

    @Test
    void listByOwner_shouldReturnEmptyList() throws Exception {
        when(documentService.getAllByOwner(5L)).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/api/docs/user/5"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[]"));
    }

    @Test
    void editContent_shouldReturnUpdated() throws Exception {
        Document updated = new Document();
        updated.setContent("C1");
        when(documentService.edit(3L, "C1")).thenReturn(updated);
        mockMvc.perform(put("/api/docs/edit/3")
                        .contentType(MediaType.TEXT_PLAIN)
                        .content("C1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("C1"));
    }

    @Test
    void get_missing_should404() throws Exception {
        when(documentService.getById(9L)).thenThrow(new RuntimeException("missing"));
        mockMvc.perform(get("/api/docs/9"))
                .andExpect(status().isNotFound());
    }
}
