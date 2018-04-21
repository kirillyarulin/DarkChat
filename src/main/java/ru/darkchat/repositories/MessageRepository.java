package ru.darkchat.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.darkchat.models.Message;

import java.util.List;
import java.util.Optional;

@Repository
public interface MessageRepository extends CrudRepository<Message,Long> {
    List<Message> findAllByChat_Id(long chatId);

    Optional<Message> findTopByChat_IdOrderByTimeDesc(long chatId);
}
