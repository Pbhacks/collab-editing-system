package com.collab.version.controller;

import com.collab.version.model.Version;
import com.collab.version.service.VersionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/versions")
@RequiredArgsConstructor
public class VersionController {
    private final VersionService service;

    // 1. Save a new version
    @PostMapping("/save")
    public ResponseEntity<Version> save(@RequestBody Version version) {
        return ResponseEntity.ok(service.saveVersion(version));
    }

    // 2. Get version history by document ID
    @GetMapping("/history/{docId}")
    public List<Version> history(@PathVariable Long docId) {
        return service.getHistory(docId);
    }

    // 3. Revert to a specific version (retrieve it by ID)
    @GetMapping("/revert/{versionId}")
    public ResponseEntity<Version> revert(@PathVariable Long versionId) {
        return ResponseEntity.ok(service.getVersionById(versionId));
    }
}
