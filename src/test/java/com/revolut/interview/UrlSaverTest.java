package com.revolut.interview;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static org.junit.jupiter.api.Assertions.*;

class UrlSaverTest {
    
    UrlSaver storage;
    ConcurrentMap<String, URL> map;
    
    @BeforeEach
    @SneakyThrows
    void setUp() {
       map = new ConcurrentHashMap<>();
       map.put("abcde", new URL("https://www.google.com/search?q=stackoverflow"));
    }
 
    @Test
    @SneakyThrows
    @DisplayName("Should return true key-value pair if the key is not taken")
    void shouldSaveIfKeyNotTaken() {
        storage = new UrlSaverImpl(new ConcurrentHashMap<>());
        
        var isSaved = storage.save("abcde", new URL("https://www.google.com/search?q=stackoverflow"));
        
        assertTrue(isSaved);
    }
    
    @Test
    @SneakyThrows
    @DisplayName("Should return false key-value pair if the key is not taken")
    void shouldRejectIfKeyTaken() {
        
        storage = new UrlSaverImpl(map);

        var isSaved = storage.save("abcde", new URL("https://www.google.com/search?q=stackoverflow"));
        
        assertFalse(isSaved);
    }

    @Test
    @SneakyThrows
    @DisplayName("Should return value by key if the key is present")
    void shouldRetrieveByKeyIfPresent() {
        storage = new UrlSaverImpl(map);
        
        var retrievedValue = storage.retrieve("abcde");

        URL expected = new URL("https://www.google.com/search?q=stackoverflow");
        assertEquals(expected, retrievedValue);
    }

    @Test
    @DisplayName("Should return null if the key is not present")
    void shouldReturnNullIfNotPresent() {
        storage = new UrlSaverImpl(new ConcurrentHashMap<>());
        
        var retrievedValue = storage.retrieve("abcde");
        
        assertNull(retrievedValue);
    }
}