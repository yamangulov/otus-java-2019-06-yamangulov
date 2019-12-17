package ru.otus.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.ref.WeakReference;
import java.util.Objects;
import java.util.WeakHashMap;

@Component
public class HwCacheImpl<K, V> implements HwCache<K, V> {
    private static Logger logger = LoggerFactory.getLogger(HwCacheImpl.class);
    private static final String PUT_ACTION = "Entity put to cache";
    private static final String REMOVE_ACTION = "Entity deleted from cache";

    private final WeakHashMap<K, V> cache;
    private WeakHashMap<K, WeakReference<HwCacheListener<K, V>>> listenersMap;

    @Autowired
    public HwCacheImpl() {
        this.cache = new WeakHashMap<>();
        this.listenersMap = new WeakHashMap<>();
    }

    @Override
    public void put(K key, V value) {
        cache.put(key, value);
        HwCacheListener<K, V> listener = new HwCacheListenerImpl<>();
        addListener(key, listener);
        listener.notify(key, value, PUT_ACTION);
    }

    @Override
    public void remove(K key) {
//        сложный метод удаления, который пришлось бы использовать, если слушатели хранить в ArrayList
//
//        V oldValue = get(key);
//        WeakReference<HwCacheListener<K, V>> weakReference = listenersMap.get(key);
//        Objects.requireNonNull(weakReference.get()).notify(key, oldValue, REMOVE_ACTION);
//        cache.remove(key);
//        removeListener(weakReference.get());

//        так как я храню слушатели также как кэш в WeakHaskMap, достаточно удалить нужный слушатель
//        по ключу
        V oldValue = get(key);
        WeakReference<HwCacheListener<K, V>> weakReference = listenersMap.get(key);
        Objects.requireNonNull(weakReference.get()).notify(key, oldValue, REMOVE_ACTION);
        cache.remove(key);
        removeListener(key);
    }

    @Override
    public V get(K key) {
        return cache.get(key);
    }

    @Override
    public void addListener(K key, HwCacheListener<K, V> listener) {
        WeakReference<HwCacheListener<K, V>> weakReference = new WeakReference<>(listener);
        listenersMap.put(key, weakReference);
    }

    @Override
    public void removeListener(HwCacheListener<K, V> listener) {
        listenersMap.forEach((key, weakReference) -> {
            if (weakReference == listener) {
                listenersMap.remove(key);
            }
        });
    }

    @Override
    public void removeListener(K key) {
        listenersMap.remove(key);
    }

    public int size() {
        return cache.size();
    }
}
