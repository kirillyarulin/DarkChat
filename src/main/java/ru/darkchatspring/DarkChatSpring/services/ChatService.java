package ru.darkchatspring.DarkChatSpring.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.darkchatspring.DarkChatSpring.models.Chat;
import ru.darkchatspring.DarkChatSpring.models.User;
import ru.darkchatspring.DarkChatSpring.repositories.ChatRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ChatService {
    private final ChatRepository chatRepository;

    @Autowired
    public ChatService(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    public List<Chat> getAllUserChats(long userId) {
        return chatRepository.findUserChats(userId);
    }

    public Chat getChat(long id) {
        return chatRepository.findById(id).orElse(null);
    }

    public boolean existsChatByParticipants(List<User> participants) {
        List<Long> ids = participants.stream()
                .map(User::getId)
                .collect(Collectors.toList());
        Optional<Chat> optionalChat = chatRepository.getChatsByParticipants(ids);
        return optionalChat.isPresent();
    }

    public void createChat(Chat chat) {
            chatRepository.save(chat);
    }

    public void updateChat(long chatId,Chat chat) {
        chat.setId(chatId);
        chatRepository.save(chat);
    }

    public void deleteChat(long chatId) {
        chatRepository.deleteById(chatId);
    }
}
