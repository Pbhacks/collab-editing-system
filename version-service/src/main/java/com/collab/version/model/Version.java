package com.collab.version.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import java.time.LocalDateTime;

@Entity
public class Version {
    @Id
    @GeneratedValue
    private Long id;
    private Long docId;
    private String editorName;
    private LocalDateTime time;
    @Lob
    private String content;

    // Getters / Setters (explicit to avoid Lombok processing issues in some environments)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getDocId() { return docId; }
    public void setDocId(Long docId) { this.docId = docId; }
    public String getEditorName() { return editorName; }
    public void setEditorName(String editorName) { this.editorName = editorName; }
    public LocalDateTime getTime() { return time; }
    public void setTime(LocalDateTime time) { this.time = time; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}
