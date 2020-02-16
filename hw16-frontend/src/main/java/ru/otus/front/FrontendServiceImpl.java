package ru.otus.front;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.otus.api.model.User;
import ru.otus.messagesystem.Message;
import ru.otus.messagesystem.MessageType;
import ru.otus.messagesystem.MsClient;
import ru.otus.messagesystem.common.MessageStr;
import ru.otus.messagesystem.common.Serializers;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

@Component
@Slf4j
public class FrontendServiceImpl implements FrontendService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final Map<UUID, Consumer<?>> consumerMap = new ConcurrentHashMap<>();
    private final MsClient msClient;
    private final String databaseServiceClientName;

    public FrontendServiceImpl(MsClient msClient, @Value("${databaseServiceClientName}") String databaseServiceClientName) {
        this.msClient = msClient;
        this.databaseServiceClientName = databaseServiceClientName;
    }

    public Map<UUID, Consumer<?>> getConsumerMap() {
        return consumerMap;
    }

    @Override
    public void saveUser(String frontMessage, Consumer<String> dataConsumer) {

        //Извлекаем JSON объект с данными пользователя из сообщения. Недостаток моей реализации через ObjectMapper в том, то Frontend Server должен знать детали POJO объектов, но я думаю, что это некритично
        User userFromMessage = getUserFromMessage(frontMessage);
        String jsonUserData = getJsonUserDataFromMessage(userFromMessage);

        Message outMsg = msClient.produceMessage(databaseServiceClientName, jsonUserData, MessageType.USER_DATA);
        consumerMap.put(outMsg.getId(), dataConsumer);
        msClient.sendMessage(outMsg);
    }

    @Override
    public void getUsersList(String frontMessage, Consumer<String> dataConsumer) {
        Message outMsg = msClient.produceMessage(databaseServiceClientName, "", MessageType.USERS_LIST);
        consumerMap.put(outMsg.getId(), dataConsumer);
        msClient.sendMessage(outMsg);
    }

    @Override
    public void getUserData(long userId, Consumer<String> dataConsumer) {
        Message outMsg = msClient.produceMessage(databaseServiceClientName, userId, MessageType.USER_DATA);
        consumerMap.put(outMsg.getId(), dataConsumer);
        msClient.sendMessage(outMsg);
    }

    @Override
    public <T> Optional<Consumer<T>> takeConsumer(UUID sourceMessageId, Class<T> tClass) {
        Consumer<T> consumer = (Consumer<T>) consumerMap.remove(sourceMessageId);
        if (consumer == null) {
            log.warn("consumer not found for:{}", sourceMessageId);
            return Optional.empty();
        }
        return Optional.of(consumer);
    }

    @Override
    public void sendFrontMessage(Message message) {
        log.info("new message:{}", message);
        try {
            String userJsonData = Serializers.deserialize(message.getPayload(), String.class);
            UUID sourceMessageId = message.getSourceMessageId();
            if (sourceMessageId == null) {
                throw new RuntimeException("Not found sourceMsg for message:" + message.getId());
            }

            takeConsumer(sourceMessageId, String.class).ifPresent(consumer -> consumer.accept(userJsonData));

        } catch (Exception ex) {
            log.error("msg:" + message, ex);
        }

    }

    @Override
    public User getUserFromMessage(String frontMessage) {
        MessageStr messageStr = null;
        try {
            messageStr = objectMapper.readValue(frontMessage, MessageStr.class);
        } catch (JsonProcessingException e) {
            log.error("Error by reading message from frontendMessage: ", e.getMessage());
        }
        return messageStr.getUserFromMessage();
    }

    @Override
    public String getJsonUserDataFromMessage(User userFromMessage) {
        String jsonUserData = null;
        try {
            jsonUserData = objectMapper.writeValueAsString(userFromMessage);
        } catch (JsonProcessingException e) {
            log.error("Error by creating jsonUserDate from userFromMessage: ", e.getMessage());
        }
        log.info("jsonUserData: {}", jsonUserData);
        return jsonUserData;
    }
}
