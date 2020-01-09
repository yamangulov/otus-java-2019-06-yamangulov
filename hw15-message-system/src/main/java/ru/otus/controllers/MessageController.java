package ru.otus.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import ru.otus.front.FrontendService;

@Slf4j
@Controller
public class MessageController {

    private FrontendService frontendService;
    private SimpMessagingTemplate messageSender;

    public MessageController(FrontendService frontendService, SimpMessagingTemplate messageSender) {
        this.frontendService = frontendService;
        this.messageSender = messageSender;
    }

    @MessageMapping("/addUser")
    public void saveUser(String frontMessage) {
        log.info("Получено сообщение от фронта: {}", frontMessage);

        frontendService.saveUser(frontMessage, userData -> {
            log.info("DBService ответил сообщением: {}", userData);
            messageSender.convertAndSend("/topic/response/addUser", userData);
        });
    }
}
