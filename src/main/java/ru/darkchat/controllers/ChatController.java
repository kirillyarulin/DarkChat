package ru.darkchat.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.darkchat.models.Chat;
import ru.darkchat.services.ChatService;
import ru.darkchat.validators.ChatValidator;


@RestController
public class ChatController {
    private final ChatService chatService;
    private final ChatValidator chatValidator;

    @Autowired
    public ChatController(ChatService chatService, ChatValidator chatValidator) {
        this.chatService = chatService;
        this.chatValidator = chatValidator;
    }


    @RequestMapping(method = RequestMethod.POST, value = "/chats")
    public ResponseEntity<Chat> createChat(@RequestBody Chat chat, BindingResult bindingResult) {
        chatValidator.validate(chat,bindingResult);

        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            chatService.createChat(chat);
            return new ResponseEntity<>(chat,HttpStatus.OK);
        }
    }

    //todo
//    @RequestMapping(method = RequestMethod.DELETE, value = "/chats/{chatId}")
//    public void deleteChat(@PathVariable long chatId) {
//        chatService.deleteChat(chatId);
//    }
}
