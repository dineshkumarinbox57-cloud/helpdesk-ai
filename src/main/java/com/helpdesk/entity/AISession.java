package com.helpdesk.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "ai_sessions")
@Data
public class AISession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "email")
    private String email;
    private Long id;

    private Long userId;

    private String title;

    @Column(length = 2000)
    private String description;

    @Column(length = 5000)
    private String aiResponse;

    private Boolean resolvedByAI;

    private String status = "OPEN";

    private LocalDateTime createdDate = LocalDateTime.now();

    public String getEmail() {
    return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    

}