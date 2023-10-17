package com.backend.support.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "chats")
public class Chat {

    public Chat(){}
    public Chat(String name) {
        this.setUserName(name);
        this.setPriorityLevel(1);
        this.setOperatorName("Ожидание...");
        this.setCreatedAt(LocalDateTime.now());
    }

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "operator")
    private String operatorName;

    @Column(name = "operator_id")
    private Long operatorId;

    @Column(name = "priority_level")
    private Integer priorityLevel;

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL, orphanRemoval = true)
    public Set<Message> messages = new HashSet<>();

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public void addMessage(Message message) {
        messages.add(message);
        message.setChat(this);
    }
    public void removeMessage(Message message) {
        messages.remove(message);
        message.setChat(null);
    }

}
