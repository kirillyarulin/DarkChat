package ru.darkchat.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.darkchat.models.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User,Long> {
    boolean existsUserByUsername(String username);
    Optional<User> findByUsername(String username);

    @Query(value = "SELECT * " +
            "FROM chat_participants cp INNER JOIN users u " +
            "ON cp.chat_id=?1 AND cp.user_id=u.user_id",
            nativeQuery = true)
    List<User> getParticipantsOfChat(long chatId);
}
