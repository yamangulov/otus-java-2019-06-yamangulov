package ru.otus.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HwCacheListenerImpl<K, V> implements HwCacheListener<K, V> {
    private static Logger logger = LoggerFactory.getLogger(HwCacheImpl.class);
    public void notify(K key, V value, String action) {
        logger.info("key:{}, value:{}, action: {}", key, value, action);
    }
}
