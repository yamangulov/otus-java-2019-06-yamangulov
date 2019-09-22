package ru.otus.jdbc.helpers;

public interface RequestBuilder<T> {
    String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS public.%s (%s);";

    String createTable(T objectData);

    String insert(T objectData);

    String update(T objectData);

    <T> T select(long id, Class<? extends T> clazz);

}
