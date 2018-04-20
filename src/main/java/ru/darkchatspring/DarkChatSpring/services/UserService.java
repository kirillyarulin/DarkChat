package ru.darkchatspring.DarkChatSpring.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.darkchatspring.DarkChatSpring.models.User;
import ru.darkchatspring.DarkChatSpring.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PhotoService photoService;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, PhotoService photoService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.photoService = photoService;
    }


    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        return users;
    }

    public User getUserById(long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    public boolean existsUserByUsername(String username) {
        return userRepository.existsUserByUsername(username);
    }

    public void createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getPhoto() == null) {
            user.setPhoto(photoService.getRandomAvatar(user.getIsMale()));
        }
        userRepository.save(user);

    }

    public boolean updateUser(long userId, User user) {
        if (userRepository.existsById(userId)) {
            user.setId(userId);
            userRepository.save(user);
            return true;
        } else {
            return false;
        }
    }

    public boolean deleteUser(long userId) {
        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
            return true;
        } else {
            return false;
        }
    }


}
