package ru.darkchatspring.DarkChatSpring.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.darkchatspring.DarkChatSpring.services.UserService;
import ru.darkchatspring.DarkChatSpring.models.User;
import ru.darkchatspring.DarkChatSpring.validators.UserValidator;

import java.util.List;

@RestController
public class UserController {
    private final UserService userService;

    private final UserValidator userValidator;

    @Autowired
    public UserController(UserService userService, UserValidator userValidator) {
        this.userService = userService;
        this.userValidator = userValidator;
    }


    @RequestMapping("/users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }


    @RequestMapping(value = "/signup",method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<User> createUser(@RequestBody User user, BindingResult bindingResult) {

        userValidator.validate(user, bindingResult);

        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            userService.createUser(user);
            return new ResponseEntity<>(user,HttpStatus.OK);
        }

    }

    //todo
//    @RequestMapping(method = RequestMethod.PUT, value = "/{userId}")
//    public void updateUser(@PathVariable long userId,@RequestBody User user) {
//        userService.updateUser(userId,user);
//    }

    //todo
//    @RequestMapping(method = RequestMethod.DELETE, value = "{userId}")
//    public void deleteUser(@PathVariable long userId) {
//        userService.deleteUser(userId);
//    }

}
