package ru.otus.jdbc.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.api.dao.EntityDao;
import ru.otus.api.service.DBServiceException;
import ru.otus.api.sessionmanager.SessionManager;
import ru.otus.jdbc.DbExecutor;
import ru.otus.jdbc.sessionmanager.SessionManagerJdbc;

import java.sql.Connection;
import java.sql.SQLException;

public class EntityDaoJdbc<T> implements EntityDao<T> {
    private static Logger logger = LoggerFactory.getLogger(EntityDaoJdbc.class);

    private final SessionManagerJdbc sessionManager;
    private final DbExecutor<T> dbExecutor;

    public EntityDaoJdbc(SessionManagerJdbc sessionManager, DbExecutor<T> dbExecutor) {
        this.sessionManager = sessionManager;
        this.dbExecutor = dbExecutor;
    }

    @Override
    public void create(T objectData) throws SQLException {
        dbExecutor.createTable(objectData);
        dbExecutor.create(objectData);
    }

    @Override
    public void update(T objectData) throws SQLException {
        dbExecutor.update(objectData);
    }

    @Override
    public void createOrUpdate(T objectData) {
        dbExecutor.createOrUpdate(objectData);
    }

    @Override
    public SessionManager getSessionManager() {
        return sessionManager;
    }

    @Override
    public <T> T load(long id, Class<T> clazz) {
        try {
            return dbExecutor.load(id, clazz);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new DBServiceException(e);
        }
    }
}
