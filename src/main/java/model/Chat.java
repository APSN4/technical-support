package model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashMap;

@Data
@Entity
@Table
public class Chat {

    public Chat() throws Exception {
        throw new Exception("Fields can not be empty!");
    }
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
    private String operatorId;

    @Column(name = "priority_level")
    private Integer priorityLevel;

    @Transient
    @Column(name = "messages")
    public HashMap<Long, String> messages = new HashMap<>();

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public void addMessage(Long Id, String text) {
        messages.put(Id, text);
    }

}
