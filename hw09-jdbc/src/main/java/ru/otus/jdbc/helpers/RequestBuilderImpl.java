package ru.otus.jdbc.helpers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.annotations.Id;
import ru.otus.annotations.Size;
import ru.otus.annotations.Type;

import java.lang.reflect.Field;

public class RequestBuilderImpl<T> implements RequestBuilder<T> {
    private static Logger logger = LoggerFactory.getLogger(RequestBuilderImpl.class);

    @Override
    public String createTable(T objectData) {
        String request = "create table if not exists " + objectData.getClass().getSimpleName() + " (";
        StringBuilder stringBuilder = new StringBuilder(request);
        Field[] fields = objectData.getClass().getDeclaredFields();
        if (!checkAnnotationsIdCount(fields)) {
            throw new NotValidClassException("Данный класс не содержит аннотацию Id либо имеет более одной аннотации Id и поэтому не может быть представлен таблицей в БД");
        }
        for (int i = 0; i < fields.length; i++) {
            String fieldName = fields[i].getName() + " ";
            String type = fields[i].getAnnotation(Type.class).type().toString();
            String size = "";
            if (fields[i].isAnnotationPresent(Size.class)) {
                size = "(" + fields[i].getAnnotation(Size.class).size() + ") ";
            }
            String notNull = "";
            String autoIncrement = "";
            if (fields[i].isAnnotationPresent(Id.class)) {
                notNull = fields[i].getAnnotation(Id.class).isNotNull() ? " not null " : "";
                autoIncrement = fields[i].getAnnotation(Id.class).isAutoIncrement() ? " auto_increment " : "";
            }
            String delimiter = (i != fields.length - 1) ? ", " : "";
            stringBuilder.append(fieldName);
            stringBuilder.append(type);
            stringBuilder.append(size);
            stringBuilder.append(notNull);
            stringBuilder.append(autoIncrement);
            stringBuilder.append(delimiter);
        }
        stringBuilder.append(");");
        request = stringBuilder.toString();

        return request;
    }

    @Override
    public String insert(T objectData) {
        String request = "insert into " + objectData.getClass().getSimpleName() + " (";
        StringBuilder stringBuilder = new StringBuilder(request);
        Field[] fields = objectData.getClass().getDeclaredFields();
        if (!checkAnnotationsIdCount(fields)) {
            throw new NotValidClassException("Данный класс не содержит аннотацию Id либо имеет более одной аннотации Id и поэтому не представлен таблицей в БД");
        }
        for (int i = 0; i < fields.length; i++) {
            String fieldName = fields[i].getName();
            String delimiter = (i != fields.length - 1) ? ", " : "";
            stringBuilder.append(fieldName);
            stringBuilder.append(delimiter);
        }
        stringBuilder.append(") values (");
        for (int i = 0; i < fields.length; i++) {
            String delimiter = (i != fields.length - 1) ? ", " : "";
            if (fields[i].isAnnotationPresent(Id.class)) {
                stringBuilder.append("default");
            } else {
                stringBuilder.append("?");
            }
            stringBuilder.append(delimiter);
        }
        stringBuilder.append(");");
        request = stringBuilder.toString();

        return request;
    }

    @Override
    public String update(T objectData) {
        String request = "update " + objectData.getClass().getSimpleName() + " set ";
        StringBuilder stringBuilder = new StringBuilder(request);
        Field[] fields = objectData.getClass().getDeclaredFields();
        if (!checkAnnotationsIdCount(fields)) {
            throw new NotValidClassException("Данный класс не содержит аннотацию Id либо имеет более одной аннотации Id и поэтому не представлен таблицей в БД");
        }
        int indexForIdAnnotatedField = 0;
        for (int i = 0; i < fields.length; i++) {
            if (fields[i].isAnnotationPresent(Id.class)) {
                indexForIdAnnotatedField = i;
            }
        }
        for (int i = 0; i < fields.length; i++) {
            if (i != indexForIdAnnotatedField) {
                String fieldName = fields[i].getName();
                String delimiter = (i != fields.length - 1) ? ", " : " ";
                stringBuilder.append(fieldName);
                stringBuilder.append(" = ?");
                stringBuilder.append(delimiter);
            }
        }
        try {
            fields[indexForIdAnnotatedField].setAccessible(true);
            stringBuilder.append("where ");
            stringBuilder.append(fields[indexForIdAnnotatedField].getName());
            stringBuilder.append(" = ");
            stringBuilder.append(fields[indexForIdAnnotatedField].getLong(objectData));
            return stringBuilder.toString();
        } catch (IllegalAccessException ex) {
            logger.error(ex.getMessage(), ex);
        }
        return null;
    }

    @Override
    public String select(long id, Class<T> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        if (!checkAnnotationsIdCount(fields)) {
            throw new NotValidClassException("Данный класс не содержит аннотацию Id либо имеет более одной аннотации Id и поэтому не представлен таблицей в БД");
        }
        String fieldAnnotatedById = "";
        for (Field field : fields) {
            if (field.isAnnotationPresent(Id.class)) {
                fieldAnnotatedById = field.getName();
            }
        }
        try {
            if (!fieldAnnotatedById.isEmpty()) {
                return "select * from " + clazz.getSimpleName() + " where " + fieldAnnotatedById + " = ?;";
            }
        } catch (NotValidClassException ex) {
            logger.error(ex.getMessage(), ex);
        }
        return null;
    }

    private boolean checkAnnotationsIdCount(Field[] fields) {
        int count = 0;
        for (Field field : fields) {
            if (field.isAnnotationPresent(Id.class)) {
                count++;
            }
        }
        if (count == 1) {
            return true;
        }
        return false;
    }
}
