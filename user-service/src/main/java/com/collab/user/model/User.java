package com.collab.user.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "users") // ✅ Avoid reserved keyword conflict
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    private String email;
}
