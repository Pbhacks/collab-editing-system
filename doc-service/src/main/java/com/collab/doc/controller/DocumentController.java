package com.collab.doc.controller;

import com.collab.doc.model.Document;
import com.collab.doc.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/docs")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService service;

    // 1. Create a new document
    @PostMapping("/create")
    public ResponseEntity<Document> create(@RequestBody Document doc) {
        return ResponseEntity.ok(service.create(doc));
    }

    // 2. Edit a document (collaborative mode)
    @PutMapping("/edit/{id}")
    public ResponseEntity<Document> edit(@PathVariable Long id, @RequestBody String newContent) {
        return ResponseEntity.ok(service.edit(id, newContent));
    }

    // 3. Track: Get all docs of a user (simulate collaboration context)
    @GetMapping("/user/{ownerId}")
    public List<Document> getUserDocs(@PathVariable Long ownerId) {
        return service.getAllByOwner(ownerId);
    }

    // 4. Get a single document by ID (needed for title display in editor)
    @GetMapping("/{id}")
    public ResponseEntity<Document> getDocumentById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }
}
