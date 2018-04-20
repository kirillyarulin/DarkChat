package ru.darkchatspring.DarkChatSpring.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.darkchatspring.DarkChatSpring.models.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User,Long> {
    boolean existsUserByUsername(String username);
    Optional<User> findByUsername(String username);
}
