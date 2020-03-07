package ru.otus.messagesystem;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.otus.messagesystem.common.Serializers;
import ru.otus.sockets.FrontendClient;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class MsClientImpl implements MsClient {

  private final String name;
  private final Map<String, UniversalMessageHandler> handlers = new ConcurrentHashMap<>();
  private final FrontendClient frontendClient;

  public MsClientImpl(String name, FrontendClient frontendClient) {
    this.name = name;
    this.frontendClient = frontendClient;
  }

  @Override
  public void addHandler(MessageType type, UniversalMessageHandler requestHandler) {
    this.handlers.put(type.getValue(), requestHandler);
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public boolean sendMessage(Message msg) {
    frontendClient.sendMessage(msg);
    return true;
  }

  @Override
  public void handle(Message msg) {
    log.info("new message:{}", msg);
    try {
      UniversalMessageHandler requestHandler = handlers.get(msg.getType());
      if (requestHandler != null) {
        requestHandler.handle(msg).ifPresent(this::sendMessage);
      } else {
        log.error("handler not found for the message type:{}", msg.getType());
      }
    } catch (Exception ex) {
      log.error("msg:" + msg, ex);
    }
  }

  @Override
  public <T> Message produceMessage(String to, T data, MessageType msgType) {
    return new Message(name, to, null, msgType.getValue(), Serializers.serialize(data));
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    MsClientImpl msClient = (MsClientImpl) o;
    return Objects.equals(name, msClient.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name);
  }
}
