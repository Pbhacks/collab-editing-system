package com.collab.version.service.error;

public class VersionNotFoundException extends RuntimeException {
    public VersionNotFoundException(Long id) {
        super("Version not found: " + id);
    }
}
