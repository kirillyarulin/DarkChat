package ru.darkchat.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.darkchat.models.Chat;
import ru.darkchat.models.User;
import ru.darkchat.services.ChatService;
import ru.darkchat.services.UserService;

import java.util.List;

@Controller
public class MainController {

    private final UserService userService;
    private final ChatService chatService;

    @Autowired
    public MainController(ChatService chatService, UserService userService) {
        this.chatService = chatService;
        this.userService = userService;
    }

    @RequestMapping("/")
    public String home(Authentication authentication, Model model) {
        List<User> allUsers = userService.getAllUsers();
        User user = userService.getUserByUsername(authentication.getName());
        List<Chat> userChats =  chatService.getAllUserChats(user.getId());

        model.addAttribute("allUsers",allUsers);
        model.addAttribute("user",user);
        model.addAttribute("userChats",userChats);

        return "home";
    }

    @RequestMapping("/signup")
    public String signup() {
        return "signup";
    }

    @RequestMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "logout", required = false) String logout,
                        @RequestParam(value = "signup", required = false) String signup,
                        Model model) {
        model.addAttribute("error", error != null);
        model.addAttribute("logout", logout != null);
        model.addAttribute("signup",signup);
        return "login";
    }

}
