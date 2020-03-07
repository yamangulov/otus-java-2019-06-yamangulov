package ru.otus.security;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.api.model.User;
import ru.otus.api.service.DBServiceEntity;

@Configuration
@AllArgsConstructor
public class AdminInitConfig {
    private final DBServiceEntity<User> serviceUser;

    @Bean(initMethod = "initUsers")
    public InitUsers initUsers() {
        return new InitUsersImpl(serviceUser);
    }
}
