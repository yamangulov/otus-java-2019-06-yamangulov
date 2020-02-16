package ru.otus.messagesystem;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ru.otus.messagesystem.common.Serializers;
import ru.otus.sockets.MessageClient;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Setter
@Slf4j
public class MsClientImpl implements MsClient {

  private String name;
  private String host;
  private int port;

  private final MessageSystem messageSystem;
  private MessageClient socketClient;
  private final Map<String, MessageHandler> handlers = new ConcurrentHashMap<>();

  public MsClientImpl(MessageSystem messageSystem) {
    this.messageSystem = messageSystem;
    this.socketClient = new MessageClient();
  }

  @PostConstruct
  private void postConstruct() {
    messageSystem.addClient(this);
  }

  @Override
  public void addHandler(MessageType type, MessageHandler messageHandler) {
    this.handlers.put(type.getValue(), messageHandler);
  }

  @Override
  public boolean sendMessage(Message msg) {
    boolean result = messageSystem.newMessage(msg);
    if (!result) {
      log.error("the last message was rejected: {}", msg);
    }
    return result;
  }

  @Override
  public void handle(Message msg) {
    log.info("new message:{}", msg);
    socketClient.sendMessage(msg, host, port);
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
