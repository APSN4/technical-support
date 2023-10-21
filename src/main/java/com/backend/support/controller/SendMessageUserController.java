package com.backend.support.controller;

import com.backend.support.model.Message;
import com.backend.support.repository.ChatRepository;
import lombok.Data;
import com.backend.support.model.Chat;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.backend.support.repository.MessageRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/window/send-message")
public class SendMessageUserController {
    @Autowired
    ChatRepository chatRepository;

    @Autowired
    MessageRepository messageRepository;

    @CrossOrigin
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> sendMessageToChatUser(@RequestBody RequestSendMessage requestSendMessage) {
        Optional<Chat> chat = chatRepository.findById(requestSendMessage.id_chat);

        if (chat.isEmpty()) return new ResponseEntity<>(new JSONObject()
                .put("message", "Error id not found").toString(), HttpStatus.NOT_FOUND);

        Message message = new Message(requestSendMessage.getUser_name(), requestSendMessage.getText());
        message.setChat(chat.get());
        Message messageCreate = messageRepository.saveAndFlush(message);

        List<Message> top50MessagesDesc = messageRepository.findTop50ByOrderByIdDesc();
        JSONArray jsonArray = new JSONArray();
        for (Message object: top50MessagesDesc) {
            JSONObject objMsg = new JSONObject()
                    .put("id", object.getId())
                    .put("message", object.getText());
            jsonArray.put(objMsg);
        }

        JSONObject entityData = new JSONObject()
                .put("id_chat", chat.get().getId())
                .put("name_user", chat.get().getUserName())
                .put("name_operator", chat.get().getOperatorName())
                .put("messages", jsonArray);

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
