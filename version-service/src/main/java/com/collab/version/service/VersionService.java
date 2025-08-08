package com.collab.version.service;

import com.collab.version.model.Version;
import com.collab.version.repository.VersionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VersionService {
    private final VersionRepository repo;

    // 1. Save a new version snapshot
    public Version saveVersion(Version v) {
        v.setTime(LocalDateTime.now());
        return repo.save(v);
    }

    // 2. View history of a document
    public List<Version> getHistory(Long docId) {
        return repo.findByDocIdOrderByTimeDesc(docId);
    }

    // 3. Revert logic — just retrieve version; client will send it to doc-service
    public Version getVersionById(Long versionId) {
        return repo.findById(versionId).orElseThrow();
    }
}
