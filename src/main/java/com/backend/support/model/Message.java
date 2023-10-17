package com.backend.support.model;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "messages")
public class Message {
    public Message(){}
    public Message(String name, String text) {
        this.setChat(chat);
        this.setName(name);
        this.setText(text);
        this.setCreatedAt(LocalDateTime.now());
    }

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Chat chat;

    @Column(name = "name")
    private String name;

    @Column(name = "text")
    private String text;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
