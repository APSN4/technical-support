package com.backend.support.controller;

import com.backend.support.model.Chat;
import com.backend.support.model.Message;
import com.backend.support.repository.ChatRepository;
import com.backend.support.repository.MessageRepository;
import lombok.Data;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("v1/window")
public class SupportDialogWindowsController {
    static final Logger log =
            LoggerFactory.getLogger(SupportDialogWindowsController.class);
    @Autowired
    ChatRepository chatRepository;

    @Autowired
    MessageRepository messageRepository;

    @PostMapping(path = "/start", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createChat(@RequestBody Request createChatRequest) {
        Chat chat = new Chat(createChatRequest.getName());
        Chat chatCreated = chatRepository.saveAndFlush(chat);

        Message message = new Message(createChatRequest.getName(), createChatRequest.getText());
        message.setChat(chatCreated);
        Message messageCreate = messageRepository.saveAndFlush(message);

        JSONArray entityMessages = new JSONArray();
        entityMessages.put(new JSONObject().put("id", messageCreate.getId().longValue()));

        JSONObject entityData = new JSONObject()
                .put("id_chat", chatCreated.getId())
                .put("name_user", chatCreated.getUserName())
                .put("name_operator", chatCreated.getOperatorName())
                .put("messages", entityMessages);

        JSONObject entitySuccess = new JSONObject()
                .put("status", "success")
                .put("message", "The application has been successfully created")
                .put("data", entityData);
        return new ResponseEntity<>(entitySuccess.toString(), HttpStatus.OK);
    }

    @PostMapping(path = "/chat/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getChat(@RequestBody RequestGetChatId requestGetChatId) {
        Optional<Chat> chat = chatRepository.findById(requestGetChatId.getId());
        if (chat.isPresent()) {
            List<Message> top50MessagesDesc = messageRepository.findTop50ByOrderByIdDesc();
            JSONArray jsonArray = new JSONArray();
            for (Message object: top50MessagesDesc) {
                JSONObject objMsg = new JSONObject()
                        .put("id", object.getId())
                        .put("message", object.getText());
                jsonArray.put(objMsg);
            }

            JSONObject entitySuccess = new JSONObject()
                    .put("id_chat", chat.get().getId().longValue())
                    .put("name_user", chat.get().getUserName())
                    .put("name_operator", chat.get().getOperatorName())
                    .put("id_operator", chat.get().getOperatorId())
                    .put("priority_level", chat.get().getPriorityLevel())
                    .put("messages", jsonArray);
            return new ResponseEntity<>(new JSONObject().put("data", entitySuccess).toString(), HttpStatus.OK);
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
