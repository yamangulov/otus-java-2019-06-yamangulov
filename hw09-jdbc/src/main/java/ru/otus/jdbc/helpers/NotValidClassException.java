package ru.otus.jdbc.helpers;

public class NotValidClassException extends RuntimeException {
    public NotValidClassException(){};

    public NotValidClassException(String message) {
        super(message);
    }
}
