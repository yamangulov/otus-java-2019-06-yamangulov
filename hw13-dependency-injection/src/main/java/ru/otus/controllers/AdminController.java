package ru.otus.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.otus.api.model.User;
import ru.otus.api.service.DBServiceEntityImplCached;

import javax.servlet.http.HttpServletRequest;

@Controller
public class AdminController {

	private DBServiceEntityImplCached<User> serviceUser;

	public AdminController(DBServiceEntityImplCached<User> serviceUser) {
		this.serviceUser = serviceUser;
	}

	@GetMapping("/admin")
	public String getLogin(Model model) {
		model.addAttribute("users", serviceUser.getUsersList());
		return "admin";
		
	}

	@GetMapping("/registration")
	public String showForm(Model model) {
		User user = new User();
		model.addAttribute("user", user);
		return "registration";
	}

	@PostMapping("/registration")
	public void saveUser(@ModelAttribute("user") User user, HttpServletRequest request) {
		serviceUser.createOrUpdateEntity(user);
	}
}
