package ru.otus.api.service;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.api.model.AddressDataSet;
import ru.otus.api.model.PhoneDataSet;
import ru.otus.api.model.User;
import ru.otus.hibernate.HibernateUtils;
import ru.otus.hibernate.dao.EntityDaoHibernate;
import ru.otus.hibernate.sessionmanager.SessionManagerHibernate;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@DisplayName("Проверям что")
class DBServiceEntityImplCachedTest {
    private final Logger logger = LoggerFactory.getLogger(DBServiceEntityImplCachedTest.class);
    private DBServiceEntity<User> dbServiceEntity;
    private DBServiceEntity<User> dbServiceEntityCached;
    private List<User> users = new ArrayList<>();
    private long duration = 0;
    private long timingStart = 0;

    @BeforeEach
    void startTiming() {
        SessionFactory sessionFactory = HibernateUtils.buildSessionFactory(
                "hibernate.cfg.xml",
                User.class,
                AddressDataSet.class,
                PhoneDataSet.class
        );
        SessionManagerHibernate sessionManager = new SessionManagerHibernate(sessionFactory);
        final var userDao = new EntityDaoHibernate<User>(sessionManager);
        this.dbServiceEntity = new DBServiceEntityImpl<>(userDao);
        this.dbServiceEntityCached = new DBServiceEntityImplCached<>(userDao);

        for (int i = 0; i < 100; i++) {
            List<PhoneDataSet> phones = new ArrayList<>();
            phones.add(new PhoneDataSet("555-555"));
            var user = new User("User#" + i, "", 20 + i, new AddressDataSet("Address"), phones);
            dbServiceEntity.createOrUpdateEntity(user);
            users.add(user);
        }
        timingStart = System.currentTimeMillis();
    }

    @AfterEach
    void endTiming() {
        duration = System.currentTimeMillis() - timingStart;
        logger.info("Duration of test is {} ms", duration);
    }

    @Test
    @DisplayName("сервис без кэширования работает корректно")
    void userDbServiceTest() {
        assertDoesNotThrow(() -> {
            for (User user : users) {
                //Искусственно замедляем обращение к БД, чтобы показать преимущество кэширования. Если этого не сделать, то с кэшированием мы увидим даже небольшую задержку чем без кэширования из-за накладных расходов при получении кэша, которые могут быть больше, чем обращение к БД H2, которая находится в оперативке и потому достаточно быстрая. С другими реляционными БД преимущество кэширования очевидно. Особенно - если объекты в БД будут еще более "жирными" и кол-во объектов будет большим. А если запросы будут сложными, то все еще хуже - запросы к БД будут еще тяжелее, и кэширование сложного запроса, если его реализовать, будет давать еще большую выгоду.
                Thread.sleep(100 , 0);
                dbServiceEntity.getEntity(user.getId(), User.class);
            }
        });
    }

    @Test
    @DisplayName("сервис с кэшированием работает корректно")
    void cachedUserDbServiceTest() {
        assertDoesNotThrow(() -> {
            for (User user : users) {
                dbServiceEntityCached.getEntity(user.getId(), User.class);
            }
        });
    }
}