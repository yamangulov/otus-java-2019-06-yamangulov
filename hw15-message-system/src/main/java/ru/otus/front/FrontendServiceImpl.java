package ru.otus.front;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.api.model.User;
import ru.otus.messagesystem.Message;
import ru.otus.messagesystem.MessageType;
import ru.otus.messagesystem.MsClient;
import ru.otus.messagesystem.common.MessageStr;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;


public class FrontendServiceImpl implements FrontendService {
    private static final Logger logger = LoggerFactory.getLogger(FrontendServiceImpl.class);
    //private final JsonParser jsonParser = new JsonParser();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final Map<UUID, Consumer<?>> consumerMap = new ConcurrentHashMap<>();
    private final MsClient msClient;
    private final String databaseServiceClientName;

    public FrontendServiceImpl(MsClient msClient, String databaseServiceClientName) {
        this.msClient = msClient;
        this.databaseServiceClientName = databaseServiceClientName;
    }

    @Override
    public void saveUser(String frontMessage, Consumer<String> dataConsumer) {
        //Извлекаем JSON объект с данными пользователя из сообщения
//        JsonObject jsonObject = jsonParser.parse(frontMessage).getAsJsonObject();
//        logger.info("jsonObject: {}", jsonObject);
//
//        JsonObject messageStr = jsonObject.getAsJsonObject("messageStr");
//        logger.info("messageStr: {}", messageStr);
//
//        String jsonUserData = messageStr.toString();

        MessageStr messageStr = null;
        try {
            messageStr = objectMapper.readValue(frontMessage, MessageStr.class);
        } catch (JsonProcessingException e) {
            logger.error("Error by reading message from frontendMessage: ", e.getMessage());
        }
        User userFromMessage = messageStr.getUserFromMessage();
        String jsonUserData = null;
        try {
            jsonUserData = objectMapper.writeValueAsString(userFromMessage);
        } catch (JsonProcessingException e) {
            logger.error("Error by creating jsonUserDate from userFromMessage: ", e.getMessage());
        }
        logger.info("jsonUserData: {}", jsonUserData);


        //Формируем Message
        Message outMsg = msClient.produceMessage(databaseServiceClientName, jsonUserData, MessageType.USER_DATA);
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
            logger.warn("consumer not found for:{}", sourceMessageId);
            return Optional.empty();
        }
        return Optional.of(consumer);
    }
}
