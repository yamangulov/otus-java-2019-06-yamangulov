package ru.otus.jdbc.helpers;

import ru.otus.annotations.Id;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Reflections<T> {
    private Class clazz;
    private String table;
    private List<Field> fields;

    public Reflections(Class<? extends T> clazz) {
        if (!Arrays.stream(clazz.getDeclaredFields()).anyMatch(field -> field.isAnnotationPresent(Id.class))) {
            throw new ReflectionsException("Класс " + clazz.getName() + " нельзя записать в БД, у него отсутствует аннотация @Id");
        }
        this.clazz = clazz;
        this.table = clazz.getSimpleName().toLowerCase();
        this.fields = getClassFields(clazz);
    }

    private List<Field> getClassFields(Class<? extends T> clazz) {
        return new ArrayList<>(Arrays.asList(clazz.getDeclaredFields()));
    }
}
