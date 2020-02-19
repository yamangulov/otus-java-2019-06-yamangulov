package ru.otus.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.api.model.User;
import ru.otus.api.service.DBServiceEntity;
import ru.otus.messagesystem.Message;
import ru.otus.messagesystem.MessageType;
import ru.otus.messagesystem.RequestHandler;
import ru.otus.messagesystem.common.Serializers;

import java.util.Optional;

public class GetUserDataRequestHandler implements RequestHandler {
    private static final Logger logger = LoggerFactory.getLogger(GetUserDataRequestHandler.class);

    private final DBServiceEntity<User> dbService;
    //private final Gson gson = new Gson();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public GetUserDataRequestHandler(DBServiceEntity<User> dbService) {
        this.dbService = dbService;
    }

    @Override
    public Optional<Message> handle(Message msg) {
        String jsonUserData = Serializers.deserialize(msg.getPayload(), String.class);

        //Получив сообщение, мы сохраняем пользователя, создав объект User из JSON строки
        //User newUser = gson.fromJson(jsonUserData, User.class);

        User newUser = null;
        try {
            newUser = objectMapper.readValue(jsonUserData, User.class);
        } catch (JsonProcessingException e) {
            logger.error("Error by creating new User from jsonUserData: ", e.getMessage());
        }
        dbService.createOrUpdateEntity(newUser);
        long savedUserID = dbService.getResultId();

        //После сохранения user-у нас есть его ID, по котому мы получим данные из кеша
        User cachedUser = dbService.getEntity(savedUserID, User.class);

        //Формируем новый JSON и сообщение для отправки во фронт-сервис
        //String savedUserData = gson.toJson(cachedUser);

        String savedUserData = null;
        try {
            savedUserData = objectMapper.writeValueAsString(cachedUser);
        } catch (JsonProcessingException e) {
            logger.error("Error by creating new json from cachedUser: ", e.getMessage());
        }

        return Optional.of(new Message(msg.getTo(), msg.getFrom(), msg.getId(),
                MessageType.USER_DATA.getValue(), Serializers.serialize(savedUserData)));
    }
}
