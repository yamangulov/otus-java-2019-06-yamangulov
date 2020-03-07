package ru.otus.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
public class LoginController {

	@RequestMapping("/login")
	public String getLogin(String frontMessage) {

		return "loginPage";
		
	}

}
