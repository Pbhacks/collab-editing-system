package com.collab.doc.service;

import com.collab.doc.model.Document;
import com.collab.doc.repository.DocumentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DocumentServiceTest {

    @Mock
    private DocumentRepository documentRepository;

    @InjectMocks
    private DocumentService documentService;

    private Document document;

    @BeforeEach
    void setUp() {
        document = new Document();
        document.setContent("Initial content");
    }

    @Test
    void getById_whenDocumentExists_shouldReturnDocument() {
        when(documentRepository.findById(1L)).thenReturn(Optional.of(document));

        Document foundDocument = documentService.getById(1L);

        assertNotNull(foundDocument);
        assertEquals("Initial content", foundDocument.getContent());
        verify(documentRepository).findById(1L);
    }

    @Test
    void getById_whenDocumentDoesNotExist_shouldThrow() {
        when(documentRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> documentService.getById(2L));
        verify(documentRepository).findById(2L);
    }

    @Test
    void create_shouldSaveAndReturnDocument() {
        when(documentRepository.save(any(Document.class))).thenReturn(document);

        Document savedDocument = documentService.create(document);

        assertNotNull(savedDocument);
        assertEquals("Initial content", savedDocument.getContent());
        verify(documentRepository).save(document);
    }

    @Test
    void edit_shouldUpdateContentAndSave() {
        when(documentRepository.findById(1L)).thenReturn(Optional.of(document));
        Document saved = new Document();
        saved.setContent("Updated");
        when(documentRepository.save(any(Document.class))).thenReturn(saved);

        Document result = documentService.edit(1L, "Updated");

        assertEquals("Updated", result.getContent());
        verify(documentRepository).findById(1L);
        verify(documentRepository).save(any(Document.class));
    }
}
