package com.revolut.interview;

import java.net.URL;

public interface UrlSaver {
    
    boolean save(String key, URL value);
    
    URL retrieve(String key);
}