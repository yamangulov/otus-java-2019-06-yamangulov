package ru.otus.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import ru.otus.front.FrontendService;
import ru.otus.front.FrontendServiceImpl;
import ru.otus.handlers.GetUserDataResponseHandler;
import ru.otus.messagesystem.MessageType;
import ru.otus.messagesystem.MsClient;
import ru.otus.messagesystem.MsClientImpl;
import ru.otus.sockets.FrontendClient;
import ru.otus.sockets.FrontendServer;

import javax.annotation.PostConstruct;

@Lazy
@Configuration
@RequiredArgsConstructor
public class AppConfig {
    @Value("${spring.profiles.active:Unknown}")
    private String activeProfile;

    @Value("${databaseServiceClientName1}")
    private String dbServiceClientName1;

    @Value("${databaseServiceClientName2}")
    private String dbServiceClientName2;

    @Value("${frontendServiceClientName1}")
    private String frontendServiceClientName1;

    @Value("${frontendServiceClientName2}")
    private String frontendServiceClientName2;

    @Value("${frontendSocketServer1.port}")
    private int frontendSocketServerPort1;

    @Value("${frontendSocketServer2.port}")
    private int frontendSocketServerPort2;

    @Bean
    public FrontendClient frontendClient() {
        return new FrontendClient();
    }

    @Bean
    @Profile("front1")
    public FrontendService frontendService1() {
        return new FrontendServiceImpl(frontendMsClient1(), dbServiceClientName1);
    }

    @Bean
    @Profile("front1")
    public MsClient frontendMsClient1() {
        MsClientImpl msClient = new MsClientImpl(frontendServiceClientName1, frontendClient());
        //msClient.addHandler(MessageType.USER_DATA, new GetUserDataResponseHandler(frontendService1()));
        return msClient;
    }

    @Bean
    @Profile("front2")
    public FrontendService frontendService2() {
        return new FrontendServiceImpl(frontendMsClient2(), dbServiceClientName2);
    }

    @Bean
    @Profile("front2")
    public MsClient frontendMsClient2() {
        MsClientImpl msClient = new MsClientImpl(frontendServiceClientName2, frontendClient());
        //msClient.addHandler(MessageType.USER_DATA, new GetUserDataResponseHandler(frontendService2()));
        return msClient;
    }

    @Bean
    @Profile("front1")
    public FrontendServer frontendServer1() {
        return new FrontendServer(frontendSocketServerPort1, frontendService1());
    }

    @Bean
    @Profile("front2")
    public FrontendServer frontendServer2() {
        return new FrontendServer(frontendSocketServerPort2, frontendService2());
    }

    @PostConstruct
    private void postConstruct() {
        if (activeProfile.equals("front1")) {
            frontendMsClient1().addHandler(MessageType.USER_DATA, new GetUserDataResponseHandler(frontendService1()));
        }
        if (activeProfile.equals("front2")) {
            frontendMsClient2().addHandler(MessageType.USER_DATA, new GetUserDataResponseHandler(frontendService2()));
        }
    }
}
