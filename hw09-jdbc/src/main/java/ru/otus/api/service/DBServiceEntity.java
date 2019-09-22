package ru.otus.api.service;

public interface DBServiceEntity<T> {
    void createEntity(T objectData);

    void updateEntity(T objectData);

    void createOrUpdateEntity(T objectData);

    <T> T getEntity(long id, Class<T> clazz);
}
