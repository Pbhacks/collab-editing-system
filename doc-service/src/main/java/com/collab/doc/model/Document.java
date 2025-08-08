package com.collab.doc.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Document {
    @Id
    @GeneratedValue
    private Long id;

    private String title;
    private Long ownerId;

    @Lob // Store large text content
    private String content;
}
