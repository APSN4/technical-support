package com.backend.support.controller;

import com.backend.support.model.Chat;
import com.backend.support.model.Message;
import com.backend.support.repository.ChatRepository;
import com.backend.support.repository.MessageRepository;
import lombok.Data;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("v1/window")
public class SupportDialogWindowsController {
    @Autowired
    ChatRepository chatRepository;

    @Autowired
    MessageRepository messageRepository;

    @PostMapping(path = "/start", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createChat(@RequestBody Request createChatRequest) {
        Chat chat = new Chat(createChatRequest.getName());
        Chat chatCreated = chatRepository.saveAndFlush(chat);
        Message message = new Message(chatCreated.getId(), createChatRequest.getName(), createChatRequest.getText());
        Message messageCreate = messageRepository.saveAndFlush(message);
        chatCreated.addMessage(messageCreate.getId(), createChatRequest.getText());

        JSONArray entityMessages = new JSONArray();
        chatCreated.messages.forEach((key, value) -> entityMessages.put(new JSONObject().put(String.valueOf(key), value)));

        JSONObject entityData = new JSONObject()
                .put("id", chatCreated.getId())
                .put("name_user", chatCreated.getUserName())
                .put("name_operator", chatCreated.getOperatorName())
                .put("messages", entityMessages);

        JSONObject entitySuccess = new JSONObject()
                .put("status", "success")
                .put("message", "The application has been successfully created")
                .put("data", entityData);
        return new ResponseEntity<>(entitySuccess.toString(), HttpStatus.OK);
    }

    @PostMapping(path = "/{id}/chat", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getChat(@RequestBody RequestGetChatId requestGetChatId) {
        Optional<Chat> chat = chatRepository.findById(requestGetChatId.getId());
        if (chat.isPresent()) {
            JSONArray jsonArray = new JSONArray();
            for (Long key: chat.get().getMessages().keySet()) {
                JSONObject objMsg = new JSONObject()
                        .put("id", key)
                        .put("message", chat.get().getMessage(key));
                jsonArray.put(objMsg);
            }

            JSONObject entitySuccess = new JSONObject()
                    .put("id", chat.get().getId().longValue())
                    .put("name_user", chat.get().getUserName())
                    .put("name_operator", chat.get().getOperatorName())
                    .put("id_operator", chat.get().getOperatorId())
                    .put("priority_level", chat.get().getPriorityLevel())
                    .put("messages", jsonArray);
            return new ResponseEntity<>(entitySuccess.toString(), HttpStatus.OK);
        }
        else {
            JSONObject entityError = new JSONObject()
                    .put("message", "Error id not found");
            return new ResponseEntity<>(entityError.toString(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/ping")
    public String pingPong() {return "pong";}


    @Data
    public static class Request {
        String name;
        String text;
    }

    @Data
    public static class RequestGetChatId {
        Long id;
    }
}
