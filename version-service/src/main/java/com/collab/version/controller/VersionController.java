package com.collab.version.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.collab.version.model.Version;
import com.collab.version.service.VersionService;
import com.collab.version.service.error.VersionNotFoundException;

@RestController
@RequestMapping("/api/versions")
public class VersionController {
    private final VersionService service;

    public VersionController(VersionService service) {
        this.service = service;
    }

    // 1. Save a new version
    @PostMapping("/save")
    public ResponseEntity<Version> save(@RequestBody Version version) {
        return ResponseEntity.ok(service.saveVersion(version));
    }

    // 2. Get version history by document ID
    @GetMapping("/history/{docId}")
    public ResponseEntity<List<Version>> history(@PathVariable Long docId) {
        return ResponseEntity.ok(service.getHistory(docId));
    }

    // 3. Revert to a specific version (retrieve it by ID)
    @GetMapping("/revert/{versionId}")
    public ResponseEntity<Version> revert(@PathVariable Long versionId) {
        try {
            return ResponseEntity.ok(service.getVersionById(versionId));
        } catch (VersionNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
