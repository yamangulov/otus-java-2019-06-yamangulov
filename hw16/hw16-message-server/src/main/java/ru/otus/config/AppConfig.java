package ru.otus.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.messagesystem.MessageSystem;
import ru.otus.messagesystem.MsClient;
import ru.otus.messagesystem.MsClientImpl;

@Configuration
@RequiredArgsConstructor
public class AppConfig {

    @Bean
    @ConfigurationProperties(prefix = "frontend")
    public MsClient frontendMsClient(MessageSystem messageSystem) {
        return new MsClientImpl(messageSystem);
    }

    @Bean
    @ConfigurationProperties(prefix = "dbserver")
    public MsClient databaseMsClient(MessageSystem messageSystem) {
        return new MsClientImpl(messageSystem);
    }


}
