package com.collab.doc.service;

import com.collab.doc.model.Document;
import com.collab.doc.repository.DocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentService {
    private final DocumentRepository repo;

    public Document create(Document doc) {
        return repo.save(doc);
    }

    public Document edit(Long id, String content) {
        Document doc = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Document not found with id: " + id));
        doc.setContent(content); // Simple overwrite logic
        return repo.save(doc);
    }

    public List<Document> getAllByOwner(Long ownerId) {
        return repo.findByOwnerId(ownerId);
    }

    // ✅ Added: Fetch document by ID (needed for displaying title in editor)
    public Document getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Document not found with id: " + id));
    }
    
}

