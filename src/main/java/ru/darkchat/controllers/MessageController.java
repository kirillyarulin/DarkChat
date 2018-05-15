package ru.darkchat.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import ru.darkchat.models.Message;
import ru.darkchat.models.User;
import ru.darkchat.services.MessageService;
import ru.darkchat.validators.MessageValidator;

import java.util.List;

@RestController
public class MessageController {

    private final MessageService messageService;
    private final MessageValidator messageValidator;

    @Autowired
    public MessageController(MessageService messageService, MessageValidator messageValidator) {
        this.messageService = messageService;
        this.messageValidator = messageValidator;
    }

    @RequestMapping("/chats/{chatId}/messages")
    public List<Message> getAllMessages(@PathVariable long chatId) {
        return messageService.getAllChatMessages(chatId);
    }


    @MessageMapping("/message")
    @SendTo("/chat/messages")
    public ResponseEntity<Message> sendMessage(Message message) {
        if (messageValidator.isValid(message)) {
            messageService.addMessage(message);
            return new ResponseEntity<>(message,HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
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
