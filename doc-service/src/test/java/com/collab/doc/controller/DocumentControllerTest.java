package com.collab.doc.controller;

import com.collab.doc.model.Document;
import com.collab.doc.service.DocumentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DocumentController.class)
class DocumentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DocumentService documentService;

    @Autowired
    private ObjectMapper objectMapper;

    private Document document;

    @BeforeEach
    void setUp() {
    document = new Document();
    // id is Long in the domain model
    document.setContent("Test Content");
    // title/ownerId are optional for these tests
    }

    @Test
    void getDocumentById_whenDocumentExists_shouldReturnDocument() throws Exception {
        // service returns a Document directly
        Document existing = new Document();
        existing.setContent("Test Content");
        // simulate persistence-assigned id
        // Note: JSON assertion will not check id field here since Lombok @Data generates getters
        when(documentService.getById(1L)).thenReturn(existing);

        mockMvc.perform(get("/api/documents/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").value("Test Content"));
    }

    @Test
    void getDocumentById_whenDocumentDoesNotExist_shouldReturnNotFound() throws Exception {
        when(documentService.getById(2L)).thenThrow(new RuntimeException("not found"));

        mockMvc.perform(get("/api/documents/2"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createDocument_shouldReturnCreatedDocument() throws Exception {
        when(documentService.create(any(Document.class))).thenReturn(document);

        mockMvc.perform(post("/api/documents")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(document)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("Test Content"));
    }

    @Test
    void updateDocument_shouldReturnUpdatedDocument() throws Exception {
        Document updatedDocument = new Document();
        updatedDocument.setContent("Updated Content");

        when(documentService.edit(1L, "Updated Content")).thenReturn(updatedDocument);

        mockMvc.perform(put("/api/documents/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedDocument)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("Updated Content"));
    }

    @Test
    void updateDocument_whenDocumentDoesNotExist_shouldReturnNotFound() throws Exception {
        Document updatedDocument = new Document();
        updatedDocument.setContent("Updated Content");

        when(documentService.edit(2L, "Updated Content")).thenThrow(new RuntimeException("not found"));

        mockMvc.perform(put("/api/documents/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedDocument)))
                .andExpect(status().isNotFound());
    }
}
