package ru.otus.hibernate.handlers;

import com.google.gson.Gson;
import ru.otus.api.model.User;
import ru.otus.api.service.DBServiceEntityImplCached;
import ru.otus.messagesystem.Message;
import ru.otus.messagesystem.MessageType;
import ru.otus.messagesystem.RequestHandler;
import ru.otus.messagesystem.common.Serializers;

import java.util.Optional;

public class GetUserDataRequestHandler implements RequestHandler {
    private final DBServiceEntityImplCached<User> dbService;
    private final Gson gson = new Gson();

    public GetUserDataRequestHandler(DBServiceEntityImplCached<User> dbService) {
        this.dbService = dbService;
    }

    @Override
    public Optional<Message> handle(Message msg) {
        String jsonUserData = Serializers.deserialize(msg.getPayload(), String.class);

        //Получив сообщение, мы сохраняем пользователя, создав объект User из JSON строки
        User newUser = gson.fromJson(jsonUserData, User.class);
        dbService.createOrUpdateEntity(newUser);
        long savedUserID = dbService.getResultId();

        //После сохранения user-у нас есть его ID, по котому мы получим данные из кеша
        User cachedUser = dbService.getEntity(savedUserID, User.class);

        //Формируем новый JSON и сообщение для отправки во фронт-сервис
        String savedUserData = gson.toJson(cachedUser);

        return Optional.of(new Message(msg.getTo(), msg.getFrom(), Optional.of(msg.getId()),
                MessageType.USER_DATA.getValue(), Serializers.serialize(savedUserData)));
    }
}
