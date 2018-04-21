package ru.darkchat.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;
import ru.darkchat.models.Message;
import ru.darkchat.services.MessageService;

import java.util.List;

@RestController
public class MessageController {

    private final MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @RequestMapping("/chats/{chatId}/messages")
    public List<Message> getAllMessages(@PathVariable long chatId) {
        return messageService.getAllChatMessages(chatId);
    }


    @MessageMapping("/message")
    @SendTo("/chat/messages")
    public Message sendMessage(Message message) {
        //todo
        messageService.addMessage(message);
        return message;
    }

    @MessageMapping("")

    @RequestMapping(value = "/messages/{messageId}", method = RequestMethod.DELETE)
    public void deleteMessage(@PathVariable long messageId) {
        messageService.deleteMessage(messageId);
    }

    @RequestMapping("/chats/{chatId}/messages/last")
    public ResponseEntity<Message> getLastMessage(@PathVariable long chatId) {
        Message message = messageService.getLastMessage(chatId);
        if (message != null) {
            return new ResponseEntity<>(message, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
