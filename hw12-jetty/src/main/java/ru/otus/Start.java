package ru.otus;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.api.dao.EntityDao;
import ru.otus.api.model.AddressDataSet;
import ru.otus.api.model.PhoneDataSet;
import ru.otus.api.model.User;
import ru.otus.api.service.DBServiceEntity;
import ru.otus.api.service.DBServiceEntityImpl;
import ru.otus.hibernate.HibernateUtils;
import ru.otus.hibernate.dao.EntityDaoHibernate;
import ru.otus.hibernate.sessionmanager.SessionManagerHibernate;

import java.util.ArrayList;
import java.util.List;

public class Start {
    private static Logger logger = LoggerFactory.getLogger(Start.class);

    public static void main(String[] args) {

        SessionFactory sessionFactory = HibernateUtils.buildSessionFactory("hibernate.cfg.xml", User.class, AddressDataSet.class, PhoneDataSet.class);

        SessionManagerHibernate sessionManager = new SessionManagerHibernate(sessionFactory);
        EntityDao<User> entityDao = new EntityDaoHibernate<>(sessionManager);
        DBServiceEntity<User> dbServiceEntity = new DBServiceEntityImpl<>(entityDao);

        AddressDataSet addressDataSet = new AddressDataSet("Lenina");
        List<PhoneDataSet> phoneDataSet = new ArrayList<>();
        phoneDataSet.add(new PhoneDataSet("555-555"));
        phoneDataSet.add(new PhoneDataSet("666-666"));

        User user1 = new User( "Вася", 55, addressDataSet, phoneDataSet);

        dbServiceEntity.createOrUpdateEntity(user1);
        User mayBeCreatedUser = dbServiceEntity.getEntity(1L, User.class);

        user1.setName("А! Нет. Это же совсем не Вася");

        dbServiceEntity.createOrUpdateEntity(user1);
        User mayBeUpdatedUser = dbServiceEntity.getEntity(1L, User.class);

        outputUser("Created user", mayBeCreatedUser);
        outputUser("Updated user", mayBeUpdatedUser);
    }

    private static void outputUser(String header, User mayBeUser) {
        System.out.println("-----------------------------------------------------------");
        System.out.println(header);
        if (mayBeUser != null) {
            System.out.println(mayBeUser.getName());
        } else {
            System.out.println("User is not created");
        }
    }
}
