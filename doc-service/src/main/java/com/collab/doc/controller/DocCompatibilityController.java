package com.collab.doc.controller;

import com.collab.doc.model.Document;
import com.collab.doc.service.DocumentService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Compatibility / façade controller exposing the REST shape the React frontend expects
 * ( /api/docs/** ) while keeping the original /api/documents endpoints (tested by existing tests).
 *
 * Frontend expects:
 *  POST   /api/docs/create          -> create new document (JSON body)
 *  GET    /api/docs/user/{ownerId}  -> list documents for a user
 *  GET    /api/docs/{id}            -> fetch document by id
 *  PUT    /api/docs/edit/{id}       -> update only the content (text/plain body)
 */
@RestController
@RequestMapping("/api/docs")
public class DocCompatibilityController {

    private final DocumentService documentService;

    public DocCompatibilityController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping("/create")
    public ResponseEntity<Document> create(@RequestBody Document doc) {
        return ResponseEntity.ok(documentService.create(doc));
    }

    @GetMapping("/user/{ownerId}")
    public ResponseEntity<List<Document>> listByOwner(@PathVariable Long ownerId) {
        return ResponseEntity.ok(documentService.getAllByOwner(ownerId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Document> get(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(documentService.getById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping(value = "/edit/{id}", consumes = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<Document> editContent(@PathVariable Long id, @RequestBody String content) {
        try {
            return ResponseEntity.ok(documentService.edit(id, content));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
