package ru.otus.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.otus.api.dao.EntityDao;
import ru.otus.api.model.User;
import ru.otus.cache.HwCache;
import ru.otus.cache.HwCacheImpl;

import java.lang.ref.WeakReference;
import java.util.List;

@Service
public class DBServiceEntityImplCached<T> extends DBServiceEntityImpl<T> {
    private final HwCache<Long, Object> cache;

    @Autowired
    public DBServiceEntityImplCached(EntityDao<T> entityDao) {
        super(entityDao);
        cache = new HwCacheImpl<>();
    }

    //этот метод не используется, в гибернейте есть исключение
    @Override
    public void createEntity(T objectData) {
        super.createEntity(objectData);
    }

    //этот метод тоже не используется в гибернейте есть исключение
    @Override
    public void updateEntity(T objectData) {
        super.updateEntity(objectData);
    }

    //используется только этот метод для сохранения и обновления, потому что так я сделал в ДЗ по гибернейту и унаследовал код оттуда
    @Override
    public void createOrUpdateEntity(T objectData) {
        super.createOrUpdateEntity(objectData);
        cache.put(super.getResultId(), new WeakReference<>(objectData));
    }

    @Override
    public T getEntity(long id, Class<T> clazz) {
        T cachedEntity = (T)cache.get(id);
        if (cachedEntity != null) {
            return cachedEntity;
        }
        return super.getEntity(id, clazz);
    }

    @Override
    public List<User> getUsersList() {
        return super.getUsersList();
    }
}
