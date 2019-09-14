package ru.otus;

public class Person {
    private String  name = "User";
    private int age = 25;

    public Person() {};
    public Person(String name) {
        this.name = name;
    }
    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }
}
