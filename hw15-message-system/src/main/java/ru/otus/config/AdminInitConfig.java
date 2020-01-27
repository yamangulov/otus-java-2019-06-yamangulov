package ru.otus.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.api.model.AddressDataSet;
import ru.otus.api.model.PhoneDataSet;
import ru.otus.api.model.User;
import ru.otus.api.service.DBServiceEntity;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class AdminInitConfig {
    private DBServiceEntity<User> serviceUser;

    public AdminInitConfig(DBServiceEntity<User> serviceUser) {
        this.serviceUser = serviceUser;
    }

    @Bean
    public void createOneUser() {
        AddressDataSet addressDataSet = new AddressDataSet("Lenina");
        List<PhoneDataSet> phoneDataSet = new ArrayList<>();
        phoneDataSet.add(new PhoneDataSet("555-555"));
        phoneDataSet.add(new PhoneDataSet("666-666"));

        User admin = new User( "admin", "{noop}111111", 50, addressDataSet, phoneDataSet); //{noop} совершенно необходим при создании пароля, так как с 5 Spring Boot и выше используется уже Password Storage Format даже для plain text

        serviceUser.createOrUpdateEntity(admin);
    }

    @Bean
    public void createTwoUser() {
        AddressDataSet addressDataSet = new AddressDataSet("Tolbukhina");
        List<PhoneDataSet> phoneDataSet = new ArrayList<>();
        phoneDataSet.add(new PhoneDataSet("777-777"));
        phoneDataSet.add(new PhoneDataSet("888-888"));

        User user = new User( "simple", "{noop}222222", 45, addressDataSet, phoneDataSet);

        serviceUser.createOrUpdateEntity(user);
    }

    @Bean
    public void checkInitialUsers() {
        System.out.println("List of initial users:");
        System.out.println(serviceUser.getUsersList().get(0).getPassword());
        System.out.println(serviceUser.getUsersList().get(1).getPassword());
    }
}
