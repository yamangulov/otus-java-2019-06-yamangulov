package ru.otus.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.otus.api.model.User;
import ru.otus.front.FrontendService;

@Controller
@Slf4j
@RequestMapping("/users")
public class AdminController {

    private FrontendService frontendService;

    private SimpMessagingTemplate messageSender;

	public AdminController(FrontendService frontendService, SimpMessagingTemplate messageSender) {
        this.frontendService = frontendService;
        this.messageSender = messageSender;
	}

	@GetMapping("/admin")
	public String getLogin(String frontMessage) {
		log.info("Получено сообщение от фронта: {}", frontMessage);

		frontendService.getUsersList(frontMessage, userData -> {
			log.info("DBService ответил сообщением: {}", userData);
			sendWebSocketMessage(userData);
		});
		return "admin";
		
	}

	@GetMapping("/registration")
	public String showForm(Model model) {
		User user = new User();
		model.addAttribute("user", user);
		return "registration";
	}

	@PostMapping("/registration")
	public void saveUser(@ModelAttribute("user") User user, String frontMessage) {
		log.info("Получено сообщение от фронта: {}", frontMessage);

		frontendService.saveUser(frontMessage, userData -> {
			log.info("DBService ответил сообщением: {}", userData);
			sendWebSocketMessage(userData);
		});
	}

	//для отправки ответного сообщения в WebSocket из DBService
	private void sendWebSocketMessage(String frontMessage) {
		messageSender.convertAndSend("/topic/DBServiceResponse", frontMessage);
	}
}
