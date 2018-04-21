package ru.darkchat.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.darkchat.models.Chat;
import ru.darkchat.models.User;
import ru.darkchat.repositories.ChatRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
