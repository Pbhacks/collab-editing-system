package com.collab.version.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.collab.version.model.Version;
import com.collab.version.repository.VersionRepository;
import com.collab.version.service.error.VersionNotFoundException;

@Service
public class VersionService {
    private final VersionRepository repo;

    public VersionService(VersionRepository repo) {
        this.repo = repo;
    }

    public Version saveVersion(Version v) {
        v.setTime(LocalDateTime.now());
        return repo.save(v);
    }

    public List<Version> getHistory(Long docId) {
        return repo.findByDocIdOrderByTimeDesc(docId);
    }

    public Version getVersionById(Long versionId) {
        return repo.findById(versionId).orElseThrow(() -> new VersionNotFoundException(versionId));
    }
}
