package ru.otus.jdbc.helpers;

public class ReflectionsException extends RuntimeException {
    public ReflectionsException(){};

    public ReflectionsException(String message) {
        super(message);
    }
}
