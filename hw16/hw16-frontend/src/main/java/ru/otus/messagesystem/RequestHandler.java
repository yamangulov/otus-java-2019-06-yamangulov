package ru.otus.messagesystem;


import java.util.Optional;

public interface RequestHandler extends UniversalMessageHandler {
  Optional<Message> handle(Message msg);
}
