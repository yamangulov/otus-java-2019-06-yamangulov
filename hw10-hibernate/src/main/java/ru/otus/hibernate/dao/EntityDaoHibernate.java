package ru.otus.hibernate.dao;


import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.api.dao.EntityDao;
import ru.otus.api.dao.EntityDaoException;
import ru.otus.api.sessionmanager.SessionManager;
import ru.otus.hibernate.sessionmanager.DatabaseSessionHibernate;
import ru.otus.hibernate.sessionmanager.SessionManagerHibernate;

import java.lang.reflect.Method;
import java.sql.SQLException;

public class EntityDaoHibernate<T> implements EntityDao<T> {
  private static Logger logger = LoggerFactory.getLogger(EntityDaoHibernate.class);

  private final SessionManagerHibernate sessionManager;

  private long resultId;

  @Override
  public long getResultId() {
    return resultId;
  }

  public EntityDaoHibernate(SessionManagerHibernate sessionManager) {
    this.sessionManager = sessionManager;
  }

  @Override
  public void create(T objectData) throws SQLException {
    try {
      throw new NoSuchMethodException();
    } catch (NoSuchMethodException e) {
      logger.error("Метод create не реализован");
    }
  }

  @Override
  public void update(T objectData) {
    try {
      throw new NoSuchMethodException();
    } catch (NoSuchMethodException e) {
      logger.error("Метод update не реализован");
    }
  }

  @Override
  public void createOrUpdate(T objectData) {
    sessionManager.beginSession();
    DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
    try {
      Session hibernateSession = currentSession.getHibernateSession();
      Method methodGetId = objectData.getClass().getDeclaredMethod("getId");
      methodGetId.setAccessible(true);
      long id = (long) methodGetId.invoke(objectData);
      if (id > 0) {
        hibernateSession.merge(objectData);
      } else {
        resultId = (long)hibernateSession.save(objectData);
      }
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      throw new EntityDaoException(e);
    }
  }

  @Override
  public T load(long id, Class<T> clazz) {
    sessionManager.beginSession();
    DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
    try {
      return currentSession.getHibernateSession().find(clazz, id);
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    }
    return null;
  }

  @Override
  public SessionManager getSessionManager() {
    return sessionManager;
  }
}
