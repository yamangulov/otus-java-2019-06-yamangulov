package ru.otus.api.service;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.api.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByName(String userName);
}
