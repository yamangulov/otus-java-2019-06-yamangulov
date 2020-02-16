package ru.otus.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.api.model.User;
import ru.otus.api.service.DBServiceEntity;
import ru.otus.hibernate.handlers.GetUserDataRequestHandler;
import ru.otus.hibernate.handlers.GetUsersListRequestHandler;
import ru.otus.messagesystem.MessageType;
import ru.otus.messagesystem.MsClient;
import ru.otus.messagesystem.MsClientImpl;
import ru.otus.sockets.DBClient;

@Configuration
@RequiredArgsConstructor
public class AppConfig {
    private final DBServiceEntity<User> dbServiceEntity;

    @Value("${databaseServiceClientName}")
    private String dbServerMsClientName;

    @Value("${messageServer.port}")
    private int messageServerPort;

    @Value("${messageServer.host}")
    private String messageServerHost;

    @Bean
    public DBClient dbClient() {
        return new DBClient();
    }

    @Bean
    public MsClient msClientImpl() {
        var dbServerMsClient = new MsClientImpl(dbServerMsClientName, dbClient());
        dbServerMsClient.addHandler(MessageType.USER_DATA, new GetUserDataRequestHandler(dbServiceEntity));
        dbServerMsClient.addHandler(MessageType.USERS_LIST, new GetUsersListRequestHandler(dbServiceEntity));
        return dbServerMsClient;
    }

}