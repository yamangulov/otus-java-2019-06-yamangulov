package ru.otus.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.otus.api.model.AddressDataSet;
import ru.otus.api.model.PhoneDataSet;
import ru.otus.api.model.User;
import ru.otus.api.service.DBServiceEntity;

import java.util.ArrayList;
import java.util.List;

@Service
public class InitUsersImpl implements InitUsers {
    private DBServiceEntity<User> serviceUser;

    @Autowired
    public InitUsersImpl(DBServiceEntity<User> serviceUser) {
        this.serviceUser = serviceUser;
    }

    public void initUsers() {
        AddressDataSet addressDataSet = new AddressDataSet("Lenina");
        List<PhoneDataSet> phoneDataSet = new ArrayList<>();
        phoneDataSet.add(new PhoneDataSet("555-555"));
        phoneDataSet.add(new PhoneDataSet("666-666"));

        User admin = new User( "admin", "{noop}111111", 50, addressDataSet, phoneDataSet); //{noop} совершенно необходим при создании пароля, так как с 5 Spring Boot и выше используется уже Password Storage Format даже для plain text

        serviceUser.createOrUpdateEntity(admin);

        AddressDataSet addressDataSet2 = new AddressDataSet("Tolbukhina");
        List<PhoneDataSet> phoneDataSet2 = new ArrayList<>();
        phoneDataSet2.add(new PhoneDataSet("777-777"));
        phoneDataSet2.add(new PhoneDataSet("888-888"));

        User user = new User( "simple", "{noop}222222", 45, addressDataSet2, phoneDataSet2);

        serviceUser.createOrUpdateEntity(user);
    }



}
