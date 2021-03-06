package ru.otus.api.dao;

import ru.otus.api.model.User;
import ru.otus.api.sessionmanager.SessionManager;

import java.sql.SQLException;
import java.util.List;

public interface EntityDao<T> {

    void create(T objectData) throws SQLException;

    void update(T objectData) throws SQLException;

    void createOrUpdate(T objectData);

    T load(long id, Class<T> clazz);

    SessionManager getSessionManager();

    long getResultId();

    List<User> getUsersList();
}
