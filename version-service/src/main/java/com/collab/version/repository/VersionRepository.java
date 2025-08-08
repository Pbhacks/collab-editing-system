package com.collab.version.repository;

import com.collab.version.model.Version;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VersionRepository extends JpaRepository<Version, Long> {
    List<Version> findByDocIdOrderByTimeDesc(Long docId);
}
