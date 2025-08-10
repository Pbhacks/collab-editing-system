package com.collab.version.controller;

import java.time.Instant;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.collab.version.service.error.VersionNotFoundException;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(VersionNotFoundException.class)
    public ResponseEntity<Map<String,Object>> handleVersionNotFound(VersionNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of(
                        "timestamp", Instant.now().toString(),
                        "error", ex.getMessage()
                ));
    }
}
