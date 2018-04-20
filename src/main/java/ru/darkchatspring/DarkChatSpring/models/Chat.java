package ru.darkchatspring.DarkChatSpring.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "chats")
public class Chat {
    @Id
    @Column(name = "chatId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("chatId")
    private long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date timeOfCreation;

    @ManyToMany(cascade = { CascadeType.MERGE }, fetch = FetchType.LAZY)
    @JoinTable(name = "chatParticipants",
        joinColumns = { @JoinColumn(name = "chatId") },
        inverseJoinColumns = { @JoinColumn(name = "userId")})
    private List<User> participants;

    @JsonProperty("isRead")
    private boolean isRead;

    public Chat() {
    }

    public Chat(long id, Date timeOfCreation, List<User> participants) {
        this.id = id;
        this.timeOfCreation = timeOfCreation;
        this.participants = participants;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getTimeOfCreation() {
        return timeOfCreation;
    }

    public void setTimeOfCreation(Date timeOfCreation) {
        this.timeOfCreation = timeOfCreation;
    }

    public List<User> getParticipants() {
        return participants;
    }

    public void setParticipants(List<User> participants) {
        this.participants = participants;
    }

    public boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(boolean isRead) {
        isRead = isRead;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Chat chat = (Chat) o;
        return id == chat.id &&
                timeOfCreation == chat.timeOfCreation &&
                Objects.equals(participants, chat.participants);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, timeOfCreation, participants);
    }

    @Override
    public String toString() {
        return "Chat{" +
                "id=" + id +
                ", timeOfCreation=" + timeOfCreation +
                ", participants=" + participants +
                ", isRead=" + isRead +
                '}';
    }
}
