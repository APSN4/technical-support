package controller;

import lombok.Data;
import model.Chat;
import model.Message;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import repository.ChatRepository;
import repository.MessageRepository;

import java.util.Optional;

@RestController
@RequestMapping("v1/window")
public class SupportDialogWindows {
    @Autowired
    ChatRepository chatRepository;

    @Autowired
    MessageRepository messageRepository;

    @CrossOrigin
    @PostMapping(path = "/start", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createChat(@RequestBody Request createChatRequest) {
        Chat chat = new Chat(createChatRequest.getName());
        Chat chatCreated = chatRepository.saveAndFlush(chat);
        Message message = new Message(createChatRequest.getName(), createChatRequest.getText());
        Message messageCreate = messageRepository.saveAndFlush(message);
        chatCreated.addMessage(messageCreate.getId(), createChatRequest.getText());

        JSONArray entityMessages = new JSONArray();
        chatCreated.messages.forEach((key, value) -> entityMessages.put(new JSONObject().put(String.valueOf(key), value)));

        JSONObject entityData = new JSONObject()
                .put("name_user", chatCreated.getUserName())
                .put("name_operator", chatCreated.getOperatorName())
                .put("messages", entityMessages);

        JSONObject entitySuccess = new JSONObject()
                .put("status", "success")
                .put("message", "The application has been successfully created")
                .put("data", entityData);
        return new ResponseEntity<>(entityData.toString(), HttpStatus.OK);
    }


    @Data
    public static class Request {
        String name;
        String text;
    }
}
