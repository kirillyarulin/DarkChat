package ru.darkchat.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.darkchat.models.Chat;
import ru.darkchat.models.Message;
import ru.darkchat.models.User;
import ru.darkchat.services.ChatService;
import ru.darkchat.services.MessageService;
import ru.darkchat.validators.MessageValidator;

import java.util.List;

@RestController
public class MessageController {

    private final MessageService messageService;
    private final MessageValidator messageValidator;
    private final ChatService chatService;

    @Autowired
    public MessageController(MessageService messageService, MessageValidator messageValidator, ChatService chatService) {
        this.messageService = messageService;
        this.messageValidator = messageValidator;
        this.chatService = chatService;
    }

    @RequestMapping("/chats/{chatId}/messages")
    public ResponseEntity<List<Message>> getAllMessages(@PathVariable long chatId, Authentication authentication) {
        boolean isAccess = chatService.getChat(chatId).getParticipants().stream()
                .anyMatch(x -> x.getUsername().equals(authentication.getName()));
        if (!isAccess) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>(messageService.getAllChatMessages(chatId),HttpStatus.OK);
    }


    @MessageMapping("/message")
    @SendTo("/chat/messages")
    public ResponseEntity<Message> sendMessage(Message message, Authentication authentication) {
        if (!message.getSender().getUsername().equals(authentication.getName())) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        if (messageValidator.isValid(message)) {
            messageService.addMessage(message);
            return new ResponseEntity<>(message,HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @MessageMapping("")
    @RequestMapping(value = "/messages/{messageId}", method = RequestMethod.DELETE)
    public void deleteMessage(@PathVariable long messageId, Authentication authentication) {
        Message message = messageService.getMessage(messageId);
        if (authentication.getName().equals(message.getSender().getUsername())) {
            messageService.deleteMessage(message.getId());
        }
    }

    @RequestMapping("/chats/{chatId}/messages/last")
    public ResponseEntity<Message> getLastMessage(@PathVariable long chatId, Authentication authentication) {
        boolean isAccess = chatService.getChat(chatId).getParticipants().stream()
                .anyMatch(x -> x.getUsername().equals(authentication.getName()));
        if (!isAccess) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Message message = messageService.getLastMessage(chatId);
        if (message != null) {
            return new ResponseEntity<>(message, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
