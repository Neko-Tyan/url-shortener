package com.revolut.interview;

import java.net.URL;
import java.util.concurrent.ConcurrentMap;

public class UrlSaverImpl implements UrlSaver {
    
    private final ConcurrentMap<String, URL> map;

    public UrlSaverImpl(ConcurrentMap<String, URL> map) {
        this.map = map;
    }

    @Override
    public boolean save(String key, URL value) {
        if (map.containsKey(key)) {
            return false;
        } else {
            map.put(key, value);
            return true;
        }
    }

    @Override
    public URL retrieve(String key) {
        return map.get(key);
    }
}