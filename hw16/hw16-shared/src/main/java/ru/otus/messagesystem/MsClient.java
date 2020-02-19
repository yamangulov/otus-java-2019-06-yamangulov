package ru.otus.messagesystem;

public interface MsClient {

  void addHandler(MessageType type, UniversalMessageHandler requestHandler);

  boolean sendMessage(Message msg);

  void handle(Message msg);

  String getName();

  <T> Message produceMessage(String to, T data, MessageType msgType);

}
