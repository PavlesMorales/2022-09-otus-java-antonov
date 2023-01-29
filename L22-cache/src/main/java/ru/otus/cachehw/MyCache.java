package ru.otus.cachehw;


import lombok.extern.slf4j.Slf4j;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

@Slf4j
public class MyCache<K, V> implements HwCache<K, V> {

    private final Map<K, V> weakHashMap = new WeakHashMap<>();
    private final List<SoftReference<HwListener<K, V>>> listeners = new ArrayList<>();

    @Override
    public void put(K key, V value) {
        weakHashMap.put(key, value);
        notifyListeners(key, value, "VALUE ADDED");
    }

    @Override
    public void remove(K key) {
        V remove = weakHashMap.remove(key);
        notifyListeners(key, remove, "VALUE REMOVED");
    }

    @Override
    public V get(K key) {
        V value = weakHashMap.get(key);
        notifyListeners(key, value, "VALUE GET");
        return value;
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        listeners.add(new SoftReference<>(listener));
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        for (var softListeners : listeners) {
            var currentListener = softListeners.get();
            if (listener.equals(currentListener)) {
                listeners.remove(softListeners);
            }
        }
    }

    private void notifyListeners(K key, V value, String notification) {
        for (var softListener : listeners) {
            var kvHwListener = softListener.get();
            if (kvHwListener != null) {
                try {
                    kvHwListener.notify(key, value, notification);
                } catch (Exception e) {
                    log.error("Error notify listener: ", e);
                }
            } else {
                listeners.remove(softListener);
            }
        }
    }

    public int size() {
        return weakHashMap.size();
    }
}
