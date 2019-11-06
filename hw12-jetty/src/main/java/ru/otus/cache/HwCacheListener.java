package ru.otus.cache;

public interface HwCacheListener<K, V> {
    void notify(K key, V value, String action);
}
