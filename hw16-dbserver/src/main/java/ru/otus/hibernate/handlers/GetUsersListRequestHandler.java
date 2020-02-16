package ru.otus.hibernate.handlers;

import com.google.gson.Gson;
import ru.otus.api.model.User;
import ru.otus.api.service.DBServiceEntity;
import ru.otus.messagesystem.Message;
import ru.otus.messagesystem.MessageType;
import ru.otus.messagesystem.RequestHandler;
import ru.otus.messagesystem.common.Serializers;

import java.util.List;
import java.util.Optional;

public class GetUsersListRequestHandler implements RequestHandler {
    private final DBServiceEntity<User> dbService;
    private final Gson gson = new Gson();

    public GetUsersListRequestHandler(DBServiceEntity<User> dbService) {
        this.dbService = dbService;
    }

    @Override
    public Optional<Message> handle(Message msg) {
        List<User> usersList = dbService.getUsersList();
        String jsonUsersList = gson.toJson(usersList);


        return Optional.of(new Message(msg.getTo(), msg.getFrom(), msg.getId(),
                MessageType.USER_DATA.getValue(), Serializers.serialize(jsonUsersList)));
    }
}
