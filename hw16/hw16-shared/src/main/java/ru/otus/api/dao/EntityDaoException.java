package ru.otus.api.dao;

public class EntityDaoException extends RuntimeException {
  public EntityDaoException(Exception ex) {
    super(ex);
  }
}
