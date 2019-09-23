package ru.otus.jdbc;

import jdk.jshell.spi.ExecutionControl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.jdbc.helpers.RequestBuilder;
import ru.otus.jdbc.helpers.RequestBuilderImpl;
import ru.otus.jdbc.sessionmanager.SessionManagerJdbc;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;

public class DbExecutor<T> {
    private static Logger logger = LoggerFactory.getLogger(DbExecutor.class);

    private RequestBuilder<T> requestBuilder;
    private final SessionManagerJdbc sessionManager;
    private final Connection connection;

    public DbExecutor(SessionManagerJdbc sessionManager) {
        this.requestBuilder =  new RequestBuilderImpl<>();
        this.sessionManager = sessionManager;
        this.connection = sessionManager.getCurrentSession().getConnection();
    }

    public void createTable(T objectData) throws SQLException {
        Savepoint savepoint = connection.setSavepoint("Попытка создать таблицу для класса " + objectData.getClass().getSimpleName());
        String request = requestBuilder.createTable(objectData);
        try (PreparedStatement pst = connection.prepareStatement(request)) {
            pst.executeUpdate();
        } catch (SQLException ex) {
            connection.rollback(savepoint);
            logger.error(ex.getMessage(), ex);
            throw ex;
        }
    }

    public void create(T objectData) throws SQLException{
        Savepoint savepoint = connection.setSavepoint("Попытка записать экземпляр класса " + objectData.getClass().getSimpleName() + " в БД");
        String request = requestBuilder.insert(objectData);
        try (PreparedStatement pst = connection.prepareStatement(request)) {
            pst.executeUpdate();
        } catch (SQLException ex) {
            connection.rollback(savepoint);
            logger.error(ex.getMessage(), ex);
            throw ex;
        }
    }

    public void update(T objectData) throws SQLException{
        Savepoint savepoint = connection.setSavepoint("Попытка обновить данные экземпляра класса " + objectData.getClass().getSimpleName() + " в БД");
        String request = requestBuilder.update(objectData);
        try (PreparedStatement pst = connection.prepareStatement(request)) {
            pst.executeUpdate();
        } catch (SQLException ex) {
            connection.rollback(savepoint);
            logger.error(ex.getMessage(), ex);
            throw ex;
        }
    }

    public void createOrUpdate(T objectData) {
        try {
            throw new ExecutionControl.NotImplementedException("Метод createOrUpdate в классе DbExecutor не реализован");
        } catch (ExecutionControl.NotImplementedException ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    public T load(long id, Class<T> clazz) throws SQLException {
        Savepoint savepoint = connection.setSavepoint("Попытка получить данные экземпляра класса " + clazz.getSimpleName() + " из БД");
        String request = requestBuilder.select(id, clazz);
        try (PreparedStatement pst = connection.prepareStatement(request)) {
            ResultSet rs = pst.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            Object[] params = new Object[rsmd.getColumnCount()];
            for (int i = 1; i < rsmd.getColumnCount(); i++) {
                String name = rsmd.getColumnName(i);
                params[i] = rs.getObject(name);
            }
            try {
                if (rs.next()) {
                    return this.newInstance(clazz, params);
                }
            } catch (SQLException e) {
                logger.error(e.getMessage(), e);
            }
            return null;
        } catch (SQLException ex) {
            connection.rollback(savepoint);
            logger.error(ex.getMessage(), ex);
            throw ex;
        }
    }

    private T newInstance(Class<T> aClass, Object[] params) {

        Constructor<T> declaredConstructor = null;
        T instance = null;

        try {
            declaredConstructor = aClass.getDeclaredConstructor();
        } catch (NoSuchMethodException ex) {
            logger.error(ex.getMessage(), ex);
        }

        if (declaredConstructor != null) {
            try {
                declaredConstructor.setAccessible(true);
                instance = declaredConstructor.newInstance(params);
            } catch (IllegalAccessException | InvocationTargetException | InstantiationException ex) {
                logger.error(ex.getMessage(), ex);
            } finally {
                declaredConstructor.setAccessible(false);
            }
        }
        return instance;
    }
}
