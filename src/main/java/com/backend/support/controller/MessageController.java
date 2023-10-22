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
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("v1/window/message")
public class MessageController {

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    ChatRepository chatRepository;

    @PostMapping(path = "/all", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getAllMessages(@RequestBody AllMessageByID allMessageByID) {
        Optional<Chat> chat = chatRepository.findById(allMessageByID.getChat_id());
        if (chat.isEmpty()) return new ResponseEntity<>(new JSONObject()
                .put("message", "Error id not found").toString(), HttpStatus.NOT_FOUND);
        List<Message> messageList = messageRepository.findAllByChatId(allMessageByID.getChat_id());
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject()
                .put("data", jsonArray);
        messageList.forEach(message -> jsonArray.put(new JSONObject().put("id", message.getId())));
        return new ResponseEntity<>(jsonObject.toString(), HttpStatus.OK);
    }

    @Data
    public static class AllMessageByID{
        Long chat_id;
    }
}
