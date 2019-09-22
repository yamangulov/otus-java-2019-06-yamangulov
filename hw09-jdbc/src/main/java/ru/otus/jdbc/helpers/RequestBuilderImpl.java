package ru.otus.jdbc.helpers;

import ru.otus.annotations.Id;
import ru.otus.annotations.Size;

import java.lang.reflect.Field;

public class RequestBuilderImpl<T> implements RequestBuilder<T> {
    @Override
    public String createTable(T objectData) {
        String request = "create table if not exists " + objectData.getClass().getSimpleName() + " (";
        StringBuilder stringBuilder = new StringBuilder(request);
        Field[] fields = objectData.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            String fieldName = fields[i].getName();
            String notNull = fields[i].getAnnotation(Id.class).isNotNull() ? " not null " : "";
            String autoIncrement = fields[i].getAnnotation(Id.class).isAutoIncrement() ? " autoincrement " : "";
            String size = "(" + fields[i].getAnnotation(Size.class).size() + ") ";
            String delimiter = (i != fields.length - 1) ? ", " : "";
            stringBuilder.append(fieldName);
            stringBuilder.append(size);
            stringBuilder.append(notNull);
            stringBuilder.append(autoIncrement);
            stringBuilder.append(delimiter);
        }
        request = stringBuilder.toString() + ");";

        return request;
    }

    @Override
    public String insert(T objectData) {
        return null;
    }

    @Override
    public String update(T objectData) {
        return null;
    }

    @Override
    public <T> T select(long id, Class<? extends T> clazz) {
        return null;
    }
}
