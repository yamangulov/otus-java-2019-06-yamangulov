package ru.otus.messagesystem;


public interface MessageHandler extends UniversalMessageHandler {
  void handle(Message message, String host, int port);
}
