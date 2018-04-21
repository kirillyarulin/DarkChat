package ru.darkchat.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.darkchat.models.Chat;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRepository extends CrudRepository<Chat,Long> {

    @Query(value = "SELECT * " +
            "FROM chat_participants cp INNER JOIN chats c " +
            "ON cp.chat_id=c.chat_id AND cp.user_id=?1",
            nativeQuery = true)
    List<Chat> findUserChats(long userId);


    @Query(value =
            "SELECT ch.chat_id,ch.time_of_creation,ch.is_read " +
            "FROM " +
            "  ( " +
            "    SELECT cp.chat_id, COUNT(*) count " +
            "    FROM CHAT_PARTICIPANTS cp " +
            "    WHERE cp.user_id IN (:ids)" +
            "    GROUP BY cp.chat_id " +
            "  ) sub1 " +
            "  INNER JOIN " +
            "  ( " +
            "  SELECT chat_id, COUNT(*) count " +
            "  FROM chat_participants " +
            "  GROUP BY chat_id " +
            "  ) sub2 " +
            " ON  sub1.chat_id=sub2.chat_id AND sub1.count=sub2.count " +
            "INNER JOIN CHATS ch " +
            "  ON sub1.chat_id=ch.chat_id;",nativeQuery = true)
    Optional<Chat> getChatsByParticipants(@Param("ids") List<Long> participants);
}
