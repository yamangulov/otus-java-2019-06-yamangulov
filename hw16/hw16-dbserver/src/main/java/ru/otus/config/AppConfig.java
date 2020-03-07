package ru.otus.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import ru.otus.api.model.User;
import ru.otus.api.service.DBServiceEntity;
import ru.otus.handlers.GetUserDataRequestHandler;
import ru.otus.handlers.GetUsersListRequestHandler;
import ru.otus.messagesystem.MessageType;
import ru.otus.messagesystem.MsClient;
import ru.otus.messagesystem.MsClientImpl;
import ru.otus.sockets.DBClient;
import ru.otus.sockets.DBServer;

@Lazy
@Configuration
@RequiredArgsConstructor
public class AppConfig {
    private final DBServiceEntity<User> dbServiceEntity;

    @Value("${databaseServiceClientName1}")
    private String dbServerMsClientName1;

    @Value("${databaseServiceClientName2}")
    private String dbServerMsClientName2;

    @Value("${messageServer.port}")
    private int messageServerPort;

    @Value("${messageServer.host}")
    private String messageServerHost;

    @Value("${dbServer1.port}")
    private int dbServerPort1;

    @Value("${dbServer2.port}")
    private int dbServerPort2;

    @Bean
    public DBClient dbClient() {
        return new DBClient();
    }

    @Bean
    @Profile("db1")
    public DBServer dbServer1() {return new DBServer(msClientImpl1(), dbServerPort1);}

    @Bean
    @Profile("db1")
    public MsClient msClientImpl1() {
        var dbServerMsClient = new MsClientImpl(dbServerMsClientName1, dbClient());
        dbServerMsClient.addHandler(MessageType.USER_DATA, new GetUserDataRequestHandler(dbServiceEntity));
        dbServerMsClient.addHandler(MessageType.USERS_LIST, new GetUsersListRequestHandler(dbServiceEntity));
        return dbServerMsClient;
    }

    @Bean
    @Profile("db2")
    public DBServer dbServer2() {return new DBServer(msClientImpl2(), dbServerPort2);}

    @Bean
    @Profile("db2")
    public MsClient msClientImpl2() {
        var dbServerMsClient = new MsClientImpl(dbServerMsClientName2, dbClient());
        dbServerMsClient.addHandler(MessageType.USER_DATA, new GetUserDataRequestHandler(dbServiceEntity));
        dbServerMsClient.addHandler(MessageType.USERS_LIST, new GetUsersListRequestHandler(dbServiceEntity));
        return dbServerMsClient;
    }

}