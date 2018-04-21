package ru.darkchat.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.darkchat.models.Chat;
import ru.darkchat.services.ChatService;


@Component
public class ChatValidator implements Validator {

    private final ChatService chatService;

    @Autowired
    public ChatValidator(ChatService chatService) {
        this.chatService = chatService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Chat.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Chat chat = (Chat) o;

        if (chatService.existsChatByParticipants(chat.getParticipants())) {
            errors.rejectValue("participants","Chat with this set of participants already exists");
        }
    }
}
