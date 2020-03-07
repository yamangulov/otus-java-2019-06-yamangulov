package ru.otus.handlers;

import lombok.extern.slf4j.Slf4j;
import ru.otus.front.FrontendService;
import ru.otus.messagesystem.Message;
import ru.otus.messagesystem.RequestHandler;
import ru.otus.messagesystem.common.Serializers;

import java.util.Optional;
import java.util.UUID;

@Slf4j
public class GetUserDataResponseHandler implements RequestHandler {

  private final FrontendService frontendService;

  public GetUserDataResponseHandler(FrontendService frontendService) {
    this.frontendService = frontendService;
  }

  @Override
  public Optional<Message> handle(Message msg) {
    log.info("new message:{}", msg);
    try {
      String userJsonData = Serializers.deserialize(msg.getPayload(), String.class);
      UUID sourceMessageId = msg.getSourceMessageId();
      if (sourceMessageId == null) {
        throw new RuntimeException("Not found sourceMsg for message:" + msg.getId());
      }

      frontendService.takeConsumer(sourceMessageId, String.class).ifPresent(consumer -> consumer.accept(userJsonData));

    } catch (Exception ex) {
      log.error("msg:" + msg, ex);
    }
    return Optional.empty();
  }
}
