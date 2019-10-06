package ru.otus.api.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import ru.otus.api.dao.EntityDao;
import ru.otus.api.model.Account;
import ru.otus.api.model.User;
import ru.otus.h2.DataSourceH2;
import ru.otus.jdbc.DbExecutor;
import ru.otus.jdbc.dao.EntityDaoJdbc;
import ru.otus.jdbc.sessionmanager.SessionManagerJdbc;

import javax.sql.DataSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DBServiceEntityImplTest {
    private DataSource dataSource = new DataSourceH2();
    private SessionManagerJdbc sessionManager = new SessionManagerJdbc(dataSource);
    private DbExecutor<User> dbExecutorForUser = new DbExecutor<>(sessionManager);
    private DbExecutor<Account> dbExecutorForAccount = new DbExecutor<>(sessionManager);
    private EntityDao<User> entityDaoForUser = new EntityDaoJdbc<>(sessionManager, dbExecutorForUser);
    private EntityDao<Account> entityDaoForAccount = new EntityDaoJdbc<>(sessionManager, dbExecutorForAccount);

    @Test
    @Order(1)
    void createAndGetEntity() {
        User originalUser = new User(1, "Andrew", 50);
        Account originalAccount = new Account(1, "admin", BigDecimal.valueOf(555));

        DBServiceEntity<User> dbServiceEntityUser;
        DBServiceEntity<Account> dbServiceEntityAccount;

        dbServiceEntityUser = new DBServiceEntityImpl<>(entityDaoForUser);
        dbServiceEntityAccount = new DBServiceEntityImpl<>(entityDaoForAccount);

        dbServiceEntityUser.createEntity(originalUser);
        dbServiceEntityAccount.createEntity(originalAccount);

        User user = dbServiceEntityUser.getEntity(1, User.class);
        Account account = dbServiceEntityAccount.getEntity(1, Account.class);

        Assertions.assertThat(originalUser).isEqualToIgnoringGivenFields(user, "id");
        Assertions.assertThat(originalAccount).isEqualToIgnoringGivenFields(account, "no");
    }

    @Test
    @Order(2)
    void updateAndGetEntity() {
        User originalUser = new User(2, "John", 40);
        Account originalAccount = new Account(2, "admin", BigDecimal.valueOf(66666));

        DBServiceEntity<User> dbServiceEntityUser;
        DBServiceEntity<Account> dbServiceEntityAccount;

        dbServiceEntityUser = new DBServiceEntityImpl<>(entityDaoForUser);
        dbServiceEntityAccount = new DBServiceEntityImpl<>(entityDaoForAccount);

        dbServiceEntityUser.createEntity(originalUser);
        dbServiceEntityAccount.createEntity(originalAccount);

        originalUser.setAge(60);
        originalAccount.setNumber(BigDecimal.valueOf(77777));

        dbServiceEntityUser.updateEntity(originalUser);
        dbServiceEntityAccount.updateEntity(originalAccount);

        User user = dbServiceEntityUser.getEntity(2, User.class);
        Account account = dbServiceEntityAccount.getEntity(2, Account.class);

        Assertions.assertThat(originalUser).isEqualToIgnoringGivenFields(user, "id");
        Assertions.assertThat(originalAccount).isEqualToIgnoringGivenFields(account, "no");
    }

}