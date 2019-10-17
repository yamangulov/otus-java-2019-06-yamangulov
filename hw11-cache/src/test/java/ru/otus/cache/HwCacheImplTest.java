package ru.otus.cache;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.api.model.AddressDataSet;
import ru.otus.api.model.PhoneDataSet;
import ru.otus.api.model.User;
import ru.otus.api.service.DBServiceEntity;
import ru.otus.api.service.DBServiceEntityImpl;
import ru.otus.api.service.DBServiceEntityImplCached;
import ru.otus.hibernate.HibernateUtils;
import ru.otus.hibernate.dao.EntityDaoHibernate;
import ru.otus.hibernate.sessionmanager.SessionManagerHibernate;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Кэш должен уметь: ")
class HwCacheImplTest {
    private static final int TEST_ELEMENTS_COUNT = 30;
    private HwCacheImpl<Integer, WeakReference<User>> cache;
    private DBServiceEntity<User> dbServiceEntity;

    @BeforeEach
    void setUp() {
        this.cache = new HwCacheImpl<>();
    }

    @Test
    @DisplayName("корректно сохранять элементы")
    void put() {
        assertDoesNotThrow(this::fillCache);
    }

    @Test
    @DisplayName("корректно удалять элементы")
    void remove() {
        fillCache();

        for (int i = 0; i < TEST_ELEMENTS_COUNT; i++) {
            cache.remove(i);
        }

        assertEquals(0, cache.size());
    }

    @Test
    @DisplayName("корректно получать элемент из кэша")
    void get() {
        List<PhoneDataSet> phones = new ArrayList<>();
        phones.add(new PhoneDataSet("555-555"));
        var user = new User("User", 20, new AddressDataSet("Address"), phones);
        cache.put(1000, new WeakReference<>(user));

        assertEquals(user, cache.get(1000).get());
    }

    @Test
    @DisplayName("очищаться при вызове сборщика мусора")
    void gcClearCache() throws InterruptedException {
        SessionFactory sessionFactory = HibernateUtils.buildSessionFactory(
                "hibernate.cfg.xml",
                User.class,
                AddressDataSet.class,
                PhoneDataSet.class
        );
        SessionManagerHibernate sessionManager = new SessionManagerHibernate(sessionFactory);
        final var userDao = new EntityDaoHibernate<User>(sessionManager);
        this.dbServiceEntity = new DBServiceEntityImpl<>(userDao);

        fillCache();

        System.gc();
        Thread.sleep(1000);

        var cachedUsersAfter = 0;
        for (int i = 0; i < TEST_ELEMENTS_COUNT; i++) {
            var user = cache.get(i).get();
            if (user != null) cachedUsersAfter++;
        }

        assertEquals(0, cachedUsersAfter);
    }

    private void fillCache() {
        for (int i = 0; i < TEST_ELEMENTS_COUNT; i++) {
            List<PhoneDataSet> phones = new ArrayList<>();
            phones.add(new PhoneDataSet("555-555"));
            var user = new User("User#" + i, 20 + i, new AddressDataSet("Address"), phones);
            cache.put(i, new WeakReference<>(user));
        }
    }
}