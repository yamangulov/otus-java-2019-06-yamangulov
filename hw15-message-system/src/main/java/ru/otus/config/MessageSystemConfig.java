package ru.otus.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.api.model.User;
import ru.otus.api.service.DBServiceEntityImplCached;
import ru.otus.front.FrontendService;
import ru.otus.front.FrontendServiceImpl;
import ru.otus.front.handlers.GetUserDataResponseHandler;
import ru.otus.hibernate.handlers.GetUserDataRequestHandler;
import ru.otus.messagesystem.*;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Configuration
public class MessageSystemConfig {
    private static final String FRONTEND_SERVICE_CLIENT_NAME = "frontendService";
    private static final String DATABASE_SERVICE_CLIENT_NAME = "databaseService";
    private final DBServiceEntityImplCached<User> dbServiceEntity;

    @Autowired
    public MessageSystemConfig(DBServiceEntityImplCached<User> dbServiceEntity) {
        this.dbServiceEntity = dbServiceEntity;
    }

    @Bean
    public MessageSystem messageSystem() {
        return new MessageSystemImpl();
    }


    @Bean
    public MsClient frontendMsClient() {
        return new MsClientImpl(FRONTEND_SERVICE_CLIENT_NAME, messageSystem());
    }

    @Bean
    public FrontendService frontendService() {
        return new FrontendServiceImpl(frontendMsClient(), DATABASE_SERVICE_CLIENT_NAME);
    }

    @Bean
    public MsClient databaseMsClient() {
        return new MsClientImpl(DATABASE_SERVICE_CLIENT_NAME, messageSystem());
    }

    @PostConstruct
    private void postConstruct() {
        frontendMsClient().addHandler(MessageType.USER_DATA, new GetUserDataResponseHandler(frontendService()));
        databaseMsClient().addHandler(MessageType.USER_DATA, new GetUserDataRequestHandler(dbServiceEntity));

        messageSystem().addClient(frontendMsClient());
        messageSystem().addClient(databaseMsClient());

    }

    @PreDestroy
    private void preDestroy() {
        try {
            messageSystem().dispose();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
