package ru.otus.jdbc;

import jdk.jshell.spi.ExecutionControl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.annotations.Id;
import ru.otus.jdbc.helpers.RequestBuilder;
import ru.otus.jdbc.helpers.RequestBuilderImpl;
import ru.otus.jdbc.sessionmanager.SessionManagerJdbc;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;

public class DbExecutor<T> {
    private static Logger logger = LoggerFactory.getLogger(DbExecutor.class);

    private RequestBuilder<T> requestBuilder;
    private final SessionManagerJdbc sessionManager;
    private Connection connection;

    public DbExecutor(SessionManagerJdbc sessionManager) {
        this.requestBuilder =  new RequestBuilderImpl<>();
        this.sessionManager = sessionManager;
    }

    public void createTable(T objectData) throws SQLException {
        sessionManager.beginSession();
        connection = sessionManager.getCurrentSession().getConnection();
        Savepoint savepoint = connection.setSavepoint("Попытка создать таблицу для класса " + objectData.getClass().getSimpleName());
        String request = requestBuilder.createTable(objectData);
        try (PreparedStatement pst = connection.prepareStatement(request)) {
            pst.execute();
        } catch (SQLException ex) {
            connection.rollback(savepoint);
            logger.error(ex.getMessage(), ex);
            throw ex;
        }
    }

    public void create(T objectData) throws SQLException{
        sessionManager.beginSession();
        connection = sessionManager.getCurrentSession().getConnection();
        Savepoint savepoint = connection.setSavepoint("Попытка записать экземпляр класса " + objectData.getClass().getSimpleName() + " в БД");
        String request = requestBuilder.insert(objectData);
        try (PreparedStatement pst = connection.prepareStatement(request, Statement.RETURN_GENERATED_KEYS)) {
            Field[] fields = objectData.getClass().getDeclaredFields();
            int counter = 0;
            for (int i = 0; i < fields.length; i++) {
                if (fields[i].isAnnotationPresent(Id.class)) {
                    continue;
                }
                counter++;
                fields[i].setAccessible(true);
                Object fieldValue = fields[i].get(objectData);
                pst.setObject(counter, fieldValue);
            }
            pst.executeUpdate();
        } catch (SQLException | IllegalAccessException ex) {
            logger.error(ex.getMessage(), ex);
            connection.rollback(savepoint);
        }
    }

    public void update(T objectData) throws SQLException{
        sessionManager.beginSession();
        connection = sessionManager.getCurrentSession().getConnection();
        Savepoint savepoint = connection.setSavepoint("Попытка обновить данные экземпляра класса " + objectData.getClass().getSimpleName() + " в БД");
        String request = requestBuilder.update(objectData);
        try (PreparedStatement pst = connection.prepareStatement(request, Statement.RETURN_GENERATED_KEYS)) {
            Field[] fields = objectData.getClass().getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                if (fields[i].isAnnotationPresent(Id.class)) {
                    continue;
                }
                try {
                    fields[i].setAccessible(true);
                    Object fieldValue = fields[i].get(objectData);
                    pst.setObject(i, fieldValue);
                } catch (IllegalAccessException ex) {
                    logger.error(ex.getMessage(), ex);
                }
            }
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
        sessionManager.beginSession();
        connection = sessionManager.getCurrentSession().getConnection();
        Savepoint savepoint = connection.setSavepoint("Попытка получить данные экземпляра класса " + clazz.getSimpleName() + " из БД и создать из них экземпляр класса");
        String request = requestBuilder.select(id, clazz);
        try (PreparedStatement pst = connection.prepareStatement(request, Statement.RETURN_GENERATED_KEYS)) {
            pst.setLong(1, id);
            ResultSet rs = pst.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            Object[] params = new Object[rsmd.getColumnCount()];
            Class[] paramsClasses = new Class[rsmd.getColumnCount()];

            try {
                if (rs.next()) {
                    for (int i = 1; i < rsmd.getColumnCount() + 1; i++) {
                        String name = rsmd.getColumnName(i);
                        params[i - 1] = rs.getObject(name);

                        paramsClasses[i - 1] = params[i - 1].getClass();
                    }
                }
                return newClassInstance(clazz, params, paramsClasses);
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

    private T newClassInstance(Class<T> aClass, Object[] params, Class[] paramsClasses) {

        Constructor<T> declaredConstructor = null;
        T instance = null;

        try {
            declaredConstructor = ((Constructor<T>) aClass.getConstructors()[0]);
        } catch (Exception ex) {
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
