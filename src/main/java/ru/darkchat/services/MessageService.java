package ru.darkchat.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.darkchat.models.Message;
import ru.darkchat.repositories.MessageRepository;

import java.util.List;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }


    public List<Message> getAllChatMessages(long chatId) {
        return messageRepository.findAllByChat_Id(chatId);
    }

    public Message getMessage(long messageId) {
        return messageRepository.findById(messageId).orElse(null);
    }

    public void addMessage(Message message) {
        messageRepository.save(message);
    }

    public void deleteMessage(long messageId) {
        messageRepository.deleteById(messageId);
    }

    public Message getLastMessage(long chatId) {
        return messageRepository.findTopByChat_IdOrderByTimeDesc(chatId).orElse(null);
    }
}
