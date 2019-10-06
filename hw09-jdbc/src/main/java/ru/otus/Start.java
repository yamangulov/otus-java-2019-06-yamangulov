package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.api.dao.EntityDao;
import ru.otus.api.model.Account;
import ru.otus.api.model.User;
import ru.otus.api.service.DBServiceEntity;
import ru.otus.api.service.DBServiceEntityImpl;
import ru.otus.h2.DataSourceH2;
import ru.otus.jdbc.DbExecutor;
import ru.otus.jdbc.dao.EntityDaoJdbc;
import ru.otus.jdbc.sessionmanager.SessionManagerJdbc;

import javax.sql.DataSource;
import java.math.BigDecimal;

public class Start {
    private static Logger logger = LoggerFactory.getLogger(Start.class);

    public static void main(String[] args) {
        DataSource dataSource = new DataSourceH2();
        SessionManagerJdbc sessionManager = new SessionManagerJdbc(dataSource);
        DbExecutor<User> dbExecutorForUser = new DbExecutor<>(sessionManager);
        DbExecutor<Account> dbExecutorForAccount = new DbExecutor<>(sessionManager);
        EntityDao<User> entityDaoForUser = new EntityDaoJdbc<>(sessionManager, dbExecutorForUser);
        EntityDao<Account> entityDaoForAccount = new EntityDaoJdbc<>(sessionManager, dbExecutorForAccount);

        User originalUser = new User(1, "Andrew", 50);
        Account originalAccount = new Account(1, "admin", BigDecimal.valueOf(555));

        DBServiceEntity<User> dbServiceEntityUser;
        DBServiceEntity<Account> dbServiceEntityAccount;

        dbServiceEntityUser = new DBServiceEntityImpl<>(entityDaoForUser);
        dbServiceEntityAccount = new DBServiceEntityImpl<>(entityDaoForAccount);

        dbServiceEntityUser.createEntity(originalUser);
        dbServiceEntityAccount.createEntity(originalAccount);

        originalUser.setAge(60);
        originalAccount.setNumber(BigDecimal.valueOf(666));

        dbServiceEntityUser.updateEntity(originalUser);
        dbServiceEntityAccount.updateEntity(originalAccount);

        User user = dbServiceEntityUser.getEntity(1, User.class);
        Account account = dbServiceEntityAccount.getEntity(1, Account.class);
        logger.info("user: {}", user.getAge());
        logger.info("account: {}", account.getNumber());

    }
}
