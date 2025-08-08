package com.collab.doc.service;

import com.collab.doc.model.Document;
import com.collab.doc.repository.DocumentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DocumentServiceTest {

    @Mock
    private DocumentRepository repo;

    @InjectMocks
    private DocumentService service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreate() {
        Document doc = new Document();
        doc.setContent("Hello World");
        when(repo.save(doc)).thenReturn(doc);

        Document created = service.create(doc);

        assertNotNull(created);
        assertEquals("Hello World", created.getContent());
        verify(repo).save(doc);
    }

    @Test
    void testEdit_existingDoc() {
        Document doc = new Document();
        doc.setId(1L);
        doc.setContent("Old content");

        when(repo.findById(1L)).thenReturn(Optional.of(doc));
        when(repo.save(any(Document.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Document edited = service.edit(1L, "New content");

        assertEquals("New content", edited.getContent());
        verify(repo).findById(1L);
        verify(repo).save(doc);
    }

    @Test
    void testEdit_nonExistingDoc() {
        when(repo.findById(2L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            service.edit(2L, "Any content");
        });

        assertTrue(ex.getMessage().contains("Document not found with id"));
        verify(repo).findById(2L);
        verify(repo, never()).save(any());
    }

    @Test
    void testGetAllByOwner() {
        List<Document> docs = List.of(
                new Document(1L, "Content1", 100L),
                new Document(2L, "Content2", 100L)
        );

        when(repo.findByOwnerId(100L)).thenReturn(docs);

        List<Document> results = service.getAllByOwner(100L);

        assertEquals(2, results.size());
        verify(repo).findByOwnerId(100L);
    }

    @Test
    void testGetById_existingDoc() {
        Document doc = new Document();
        doc.setId(1L);
        doc.setContent("Some content");

        when(repo.findById(1L)).thenReturn(Optional.of(doc));

        Document result = service.getById(1L);

        assertEquals(1L, result.getId());
        verify(repo).findById(1L);
    }

    @Test
    void testGetById_nonExistingDoc() {
        when(repo.findById(5L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            service.getById(5L);
        });

        assertTrue(ex.getMessage().contains("Document not found with id"));
        verify(repo).findById(5L);
    }
}
