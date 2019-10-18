package ru.otus.cache;

import java.util.Optional;

public interface HwCache<K, V> {
    void put(K key, V value);

    void remove(K key);

    V get(K key);

    void addListener(K key, HwCacheListener<K, V> listener);

    void removeListener(HwCacheListener<K, V> listener);

    //дополнительный метод для удаления слушателя по ключу
    void removeListener(K key);
}
