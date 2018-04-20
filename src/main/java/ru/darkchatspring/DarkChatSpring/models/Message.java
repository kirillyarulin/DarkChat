package ru.darkchatspring.DarkChatSpring.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "messages")
public class Message {
    @Id
    @Column(name = "messageId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("messageId")
    private long id;

    @ManyToOne
    @JoinColumn(name="senderId")
    private User sender;

    @ManyToOne
    @JoinColumn(name="chatId")
    private Chat chat;

    @Column(name = "content",nullable = false)
    private String content;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "time",nullable = false)
    private Date time;

    @Column(name = "isRead",nullable = false)
    private boolean isRead;

    public Message() {
    }

    public Message(User sender, Chat chat, String content, Date time, boolean isRead) {
        this.sender = sender;
        this.chat = chat;
        this.content = content;
        this.time = time;
        this.isRead = isRead;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return id == message.id &&
                time == message.time &&
                Objects.equals(sender, message.sender) &&
                Objects.equals(chat, message.chat) &&
                Objects.equals(content, message.content);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, sender, chat, content, time);
    }
}
