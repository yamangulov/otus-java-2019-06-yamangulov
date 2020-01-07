package ru.otus.config;

import org.springframework.context.annotation.Configuration;
import ru.otus.api.model.AddressDataSet;
import ru.otus.api.model.PhoneDataSet;
import ru.otus.api.model.User;
import ru.otus.api.service.DBServiceEntity;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class AdminInitConfig {
    private DBServiceEntity<User> serviceUser;

    public AdminInitConfig(DBServiceEntity<User> serviceUser) {
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
}
