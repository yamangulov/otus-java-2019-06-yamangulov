package ru.otus.messagesystem;

import java.util.Optional;

public interface UniversalMessageHandler {
    Optional<Message> handle(Message msg);
}
