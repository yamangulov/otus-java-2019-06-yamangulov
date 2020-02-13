package ru.otus.messagesystem;


public interface MessageHandler {
  void handle(Message message, String host, int port);
}
