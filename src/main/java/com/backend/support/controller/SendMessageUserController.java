package com.backend.support.controller;

import com.backend.support.model.Message;
import com.backend.support.repository.ChatRepository;
import lombok.Data;
import com.backend.support.model.Chat;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.backend.support.repository.MessageRepository;

import java.util.Optional;

@RestController
@RequestMapping("/v1/window/send-message")
public class SendMessageUserController {
    @Autowired
    ChatRepository chatRepository;

    @Autowired
    MessageRepository messageRepository;

    @CrossOrigin
    @PostMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> sendMessageToChatUser(@PathVariable String id, @RequestBody RequestSendMessage requestSendMessage) {
        Optional<Chat> chat = chatRepository.findById(Long.valueOf(id));

        if (chat.isEmpty()) return new ResponseEntity<>(new JSONObject()
                .put("message", "Error id not found").toString(), HttpStatus.NOT_FOUND);

        Message message = new Message(Long.valueOf(id), requestSendMessage.getUser_name(), requestSendMessage.getText());
        Message messageCreate = messageRepository.saveAndFlush(message);

        chat.get().addMessage(messageCreate.getId(), messageCreate.getText());

        JSONObject entityData = new JSONObject()
                .put("id", messageCreate.getId())
                .put("chat_id", messageCreate.getChatId())
                .put("name", messageCreate.getName())
                .put("text", messageCreate.getText())
                .put("created_at", messageCreate.getCreatedAt());

        JSONObject entitySuccess = new JSONObject()
                .put("status", "success")
                .put("message", "The application has been successfully created")
                .put("data", entityData);
        return new ResponseEntity<>(entitySuccess.toString(), HttpStatus.OK);
    }

    @Data
    public static class RequestSendMessage {
        Long id_chat;
        String user_name;
        String text;
    }
}
