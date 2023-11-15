package com.revolut.interview;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class UrlSaverTest {
    
    UrlSaver storage;
 
    @Test
    @SneakyThrows
    @DisplayName("Should return true key-value pair if the key is not taken")
    void shouldSaveIfKeyNotTaken() {
        storage = new UrlSaverImpl(new HashMap<>());
        
        var isSaved = storage.save("abcde", new URL("https://www.google.com/search?q=stackoverflow"));
        
        assertTrue(isSaved);
    }
    
    @Test
    @SneakyThrows
    @DisplayName("Should return false key-value pair if the key is not taken")
    void shouldRejectIfKeyTaken() {
        var value = new URL("https://www.google.com/search?q=stackoverflow");
        var key = "abcde";
        storage = new UrlSaverImpl(Map.of(key, value));

        var isSaved = storage.save(key, value);
        
        assertFalse(isSaved);
    }

    @Test
    @SneakyThrows
    @DisplayName("Should return value by key if the key is present")
    void shouldRetrieveByKeyIfPresent() {
        var value = new URL("https://www.google.com/search?q=stackoverflow");
        var key = "abcde";
        storage = new UrlSaverImpl(Map.of(key, value));
        
        var retrievedValue = storage.retrieve(key);
        
        assertEquals(value, retrievedValue);
    }

    @Test
    @DisplayName("Should return null if the key is not present")
    void shouldReturnNullIfNotPresent() {
        storage = new UrlSaverImpl(new HashMap<>());
        
        var retrievedValue = storage.retrieve("abcde");
        
        assertNull(retrievedValue);
    }
}