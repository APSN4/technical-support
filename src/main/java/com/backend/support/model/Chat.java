package com.backend.support.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashMap;

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

    @Transient
    @Column(name = "messages", columnDefinition = "jsonb")
    public HashMap<Long, String> messages = new HashMap<>();

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public void addMessage(Long Id, String text) {
        messages.put(Id, text);
    }
    public String getMessage(Long id) {return messages.get(id);}

}
