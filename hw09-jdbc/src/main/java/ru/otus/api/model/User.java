package ru.otus.api.model;

import ru.otus.annotations.Id;
import ru.otus.annotations.Size;
import ru.otus.annotations.Type;
import ru.otus.jdbc.helpers.H2DataType;

public class User {
    @Id
    @Type(type = H2DataType.BIGINT)
    @Size(size = 20)
    private long id;

    @Type
    @Size
    private String name;

    @Type(type = H2DataType.INT)
    @Size(size = 3)
    private int age;

    public User(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
