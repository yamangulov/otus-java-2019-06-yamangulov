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
    @ConfigurationProperties(prefix = "frontend1")
    public MsClient frontendMsClient1(MessageSystem messageSystem) {
        return new MsClientImpl(messageSystem);
    }

    @Bean
    @ConfigurationProperties(prefix = "frontend2")
    public MsClient frontendMsClient2(MessageSystem messageSystem) {
        return new MsClientImpl(messageSystem);
    }

    @Bean
    @ConfigurationProperties(prefix = "dbserver1")
    public MsClient databaseMsClient1(MessageSystem messageSystem) {
        return new MsClientImpl(messageSystem);
    }

    @Bean
    @ConfigurationProperties(prefix = "dbserver2")
    public MsClient databaseMsClient2(MessageSystem messageSystem) {
        return new MsClientImpl(messageSystem);
    }


}
