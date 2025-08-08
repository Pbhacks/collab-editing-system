package com.collab.version.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Version {
    @Id
    @GeneratedValue
    private Long id;

    private Long docId;           // The ID of the document being versioned
    private String editorName;    // Who made the change
    private LocalDateTime time;   // When the version was saved

    @Lob
    private String content;       // Full content snapshot
}
