package ru.otus.api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.api.dao.EntityDao;
import ru.otus.api.sessionmanager.SessionManager;

public class DBServiceEntityImpl<T> implements DBServiceEntity<T> {

    private static Logger logger = LoggerFactory.getLogger(DBServiceEntityImpl.class);

    private final EntityDao<T> entityDao;

    public DBServiceEntityImpl(EntityDao<T> entityDao) {
        this.entityDao = entityDao;
    }

    @Override
    public void createEntity(T objectData) {
        try(SessionManager sessionManager = entityDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                entityDao.create(objectData);
                sessionManager.commitSession();
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                sessionManager.rollbackSession();
                throw new DBServiceException(e);
            }
        }
    }

    @Override
    public void updateEntity(T objectData) {
        try(SessionManager sessionManager = entityDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                entityDao.update(objectData);
                sessionManager.commitSession();
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                sessionManager.rollbackSession();
                throw new DBServiceException(e);
            }
        }
    }

    @Override
    public void createOrUpdateEntity(T objectData) {
        try(SessionManager sessionManager = entityDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                entityDao.createOrUpdate(objectData);
                sessionManager.commitSession();
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                sessionManager.rollbackSession();
                throw new DBServiceException(e);
            }
        }
    }

    @Override
    public T getEntity(long id, Class<T> clazz) {
        try(SessionManager sessionManager = entityDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                T objectData = entityDao.load(id, clazz);
                return objectData;
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                sessionManager.rollbackSession();
                throw new DBServiceException(e);
            }
        }
    }
}
