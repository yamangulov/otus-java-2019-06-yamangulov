package ru.otus.config;

import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import ru.otus.api.dao.EntityDao;
import ru.otus.api.model.AddressDataSet;
import ru.otus.api.model.PhoneDataSet;
import ru.otus.api.model.User;
import ru.otus.api.service.DBServiceEntity;
import ru.otus.api.service.DBServiceEntityImplCached;
import ru.otus.hibernate.HibernateUtils;
import ru.otus.hibernate.dao.EntityDaoHibernate;
import ru.otus.hibernate.sessionmanager.SessionManagerHibernate;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableTransactionManagement
@ImportResource({"classpath:hibernate.cfg.xml"})
public class HibernateConfig {

    @Bean
    public SessionFactory sessionFactory() {
        SessionFactory sessionFactory = HibernateUtils.buildSessionFactory("hibernate.cfg.xml", User.class, AddressDataSet.class, PhoneDataSet.class);

        SessionManagerHibernate sessionManager = new SessionManagerHibernate(sessionFactory);
        EntityDao<User> entityDao = new EntityDaoHibernate<>(sessionManager);
        DBServiceEntity<User> dbServiceEntity = new DBServiceEntityImplCached<>(entityDao);

        AddressDataSet addressDataSet = new AddressDataSet("Lenina");
        List<PhoneDataSet> phoneDataSet = new ArrayList<>();
        phoneDataSet.add(new PhoneDataSet("555-555"));
        phoneDataSet.add(new PhoneDataSet("666-666"));

        User admin = new User( "admin", "111111", 50, addressDataSet, phoneDataSet);

        dbServiceEntity.createOrUpdateEntity(admin);
        return sessionFactory;
    }


}
