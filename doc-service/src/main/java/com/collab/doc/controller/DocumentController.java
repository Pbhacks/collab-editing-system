package com.collab.doc.controller;

import com.collab.doc.model.Document;
import com.collab.doc.service.DocumentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    // Create a new document
    @PostMapping
    public ResponseEntity<Document> create(@RequestBody Document doc) {
        return ResponseEntity.ok(documentService.create(doc));
    }

    // Update an existing document by id
    @PutMapping("/{id}")
    public ResponseEntity<Document> update(@PathVariable Long id, @RequestBody Document updatedDocument) {
        try {
            Document updated = documentService.edit(id, updatedDocument.getContent());
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Get a single document by ID
    @GetMapping("/{id}")
    public ResponseEntity<Document> getDocumentById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(documentService.getById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
