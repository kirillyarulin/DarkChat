package ru.darkchat.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.darkchat.models.Message;
import ru.darkchat.services.UserService;

/**
 * Created by Kirill Yarulin on 15.05.18
 */
@Component
public class MessageValidator {

    private final UserService userService;

    @Autowired
    public MessageValidator(UserService userService) {
        this.userService = userService;
    }

    public boolean isValid(Message message) {

        return message.getContent() != null && !message.getContent().trim().equals("")
                && userService.existsUserByUsername(message.getSender().getUsername());
    }
}
