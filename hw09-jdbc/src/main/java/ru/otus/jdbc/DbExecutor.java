package ru.otus.jdbc;

import jdk.jshell.spi.ExecutionControl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.jdbc.helpers.RequestBuilder;
import ru.otus.jdbc.helpers.RequestBuilderImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DbExecutor<T> {
    private static Logger logger = LoggerFactory.getLogger(DbExecutor.class);

    private RequestBuilder<T> requestBuilder;

    public DbExecutor() {
        this.requestBuilder =  new RequestBuilderImpl<T>();
    }

    public void createTable(Connection connection, T objectData) throws SQLException {
        String request = requestBuilder.createTable(objectData);
        try (PreparedStatement pst = connection.prepareStatement(request)) {
            pst.executeUpdate();
        }
    }

    public void create(T objectData) {

    }

    public void update(T objectData) {

    }

    public void createOrUpdate(T objectData) {
        try {
            throw new ExecutionControl.NotImplementedException("Метод createOrUpdate в классе DbExecutor не реализован");
        } catch (ExecutionControl.NotImplementedException e) {
            logger.error(e.getMessage(), e);
        }
    }

    public <T> T load(long id, Class<T> clazz) {
        return null;
    }
}
