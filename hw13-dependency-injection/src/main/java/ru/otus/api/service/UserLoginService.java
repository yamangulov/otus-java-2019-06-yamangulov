package ru.otus.api.service;

import ru.otus.api.dao.EntityDao;
import ru.otus.api.model.User;

import java.util.Map;
import java.util.Optional;

public class UserLoginService {
    private final EntityDao<User> userEntityDao;
    private final Long adminId;

    public UserLoginService(Map<Long, EntityDao<User>> startedMap) {
        adminId = (Long) startedMap.keySet().toArray()[0];
        this.userEntityDao = startedMap.get(adminId);
    }

    public boolean authenticate(String name, String password) {
        return Optional.ofNullable(userEntityDao.load(adminId, User.class))
                .map(user -> (user.getPassword().equals(password) && user.getName().equals(name)))
                .orElse(false);
    }
}
