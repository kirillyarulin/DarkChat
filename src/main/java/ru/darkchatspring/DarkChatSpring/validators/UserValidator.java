package ru.darkchatspring.DarkChatSpring.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import ru.darkchatspring.DarkChatSpring.models.User;
import ru.darkchatspring.DarkChatSpring.services.UserService;

@Component
public class UserValidator implements Validator {

    private final UserService userService;

    @Autowired
    public UserValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        User user = (User) o;

        if (!user.getUsername().matches("^[A-Za-z][A-Za-z0-9_\\-.]{2,}$")) {
            errors.rejectValue("username","Invalid username");
        }

        if (userService.existsUserByUsername(user.getUsername())) {
            errors.rejectValue("username","A user with that username already exists");
        }

        if (user.getPassword().length() < 6 || user.getPassword().length() > 64) {
            errors.rejectValue("password","Password must be between 6 and 64 characters");
        }

        if (!user.getPassword().equals(user.getConfirmPassword())) {
            errors.rejectValue("confirmPassword", "Password does not match the confirm password");
        }
    }
}
