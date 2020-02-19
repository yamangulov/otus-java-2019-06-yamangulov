package ru.otus.api.service;

public class DBServiceException extends RuntimeException {
    public DBServiceException(Exception e) {
        super(e);
    }
}
