package ru.otus.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.otus.api.model.AddressDataSet;
import ru.otus.api.model.PhoneDataSet;
import ru.otus.api.model.User;
import ru.otus.api.service.DBServiceEntity;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
public class AdminController {

	private DBServiceEntity<User> serviceUser;

	public AdminController(DBServiceEntity<User> serviceUser) {
		this.serviceUser = serviceUser;
	}

	@PostConstruct
    public void createOneUser() {
        AddressDataSet addressDataSet = new AddressDataSet("Lenina");
        List<PhoneDataSet> phoneDataSet = new ArrayList<>();
        phoneDataSet.add(new PhoneDataSet("555-555"));
        phoneDataSet.add(new PhoneDataSet("666-666"));

        User admin = new User( "admin", "111111", 50, addressDataSet, phoneDataSet);

        serviceUser.createOrUpdateEntity(admin);
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
