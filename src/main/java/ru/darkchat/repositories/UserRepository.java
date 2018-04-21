package ru.darkchat.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.darkchat.models.User;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User,Long> {
    boolean existsUserByUsername(String username);
    Optional<User> findByUsername(String username);
}
