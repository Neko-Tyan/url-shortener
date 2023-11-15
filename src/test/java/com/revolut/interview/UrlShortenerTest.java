package com.revolut.interview;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

import org.apache.commons.lang3.StringUtils;

class UrlShortenerTest {
    
    UrlShortener urlShortener = new UrlShortenerImpl(new UrlSaverImpl(new HashMap<>()));

    public static Stream<String> getValidInput() {
        return Stream.of("https://www.google.com/search?q=stackoverflow");
    }

    public static Stream<String> getInvalidInput() {
        return Stream.of("whatever", "google.com", "1234", "", " ", null);
    }

    @ParameterizedTest
    @MethodSource("getValidInput")
    @DisplayName("Should return not null result")
    void notNullResult(String input) {
        var shortenedUrl = urlShortener.shortenUrl(input);
        
        assertNotNull(shortenedUrl);
    }
    
    @ParameterizedTest
    @MethodSource("getValidInput")
    @DisplayName("Result shouldn't be equal to the input")
    void differsFromInput(String input) {
        var shortenedUrl = urlShortener.shortenUrl(input);

        assertNotEquals(input, shortenedUrl.toString());
    }
    
    @ParameterizedTest
    @MethodSource("getValidInput")
    @DisplayName("Result should have correct protocol")
    void containsCorrectProtocol(String input) {
        var shortenedUrl = urlShortener.shortenUrl(input);
        
        assertEquals("https", shortenedUrl.getProtocol());
    }
    
    @ParameterizedTest
    @MethodSource("getValidInput")
    @DisplayName("Result should have correct host")
    void containsCorrectHost(String input) {
        var shortenedUrl = urlShortener.shortenUrl(input);
        
        assertEquals("sh.rt", shortenedUrl.getHost());
    }
    
    @ParameterizedTest
    @MethodSource("getInvalidInput")
    @DisplayName("Only valid URL input should be accepted")
    void exceptionOnInvalidUrl(String input) {
        assertThrows(MalformedURLException.class, () -> urlShortener.shortenUrl(input));
    }
    
    @ParameterizedTest
    @MethodSource("getValidInput")
    @DisplayName("Result path should have leading slash")
    void containsLeadingSlash(String input) {
        var shortenedUrl = urlShortener.shortenUrl(input);
        
        assertEquals('/', shortenedUrl.getPath().charAt(0));
    }
    
    @ParameterizedTest
    @MethodSource("getValidInput")
    @DisplayName("Result path should have correct lengths")
    void hasCorrectLengths(String input) {
        var shortenedUrl = urlShortener.shortenUrl(input);

        assertEquals(6, shortenedUrl.getPath().length());
    }
    
    @ParameterizedTest
    @MethodSource("getValidInput")
    @DisplayName("Result path should be alphanumeric after the slash")
    void pathIsAlphaNumeric(String input) {
        var shortenedUrl = urlShortener.shortenUrl(input);
        String path = shortenedUrl.getPath().substring(1); // skip slash
        
        assertTrue(StringUtils.isAlphanumeric(path));
    }

    @ParameterizedTest
    @MethodSource("getValidInput")
    @DisplayName("Result path should not be repeated over executions")
    void pathIsNotRepeated(String input) {
        var storageSet = new HashSet<String>();
        
        for (int i = 0; i < 100; i++) {
            var shortenedUrl = urlShortener.shortenUrl(input);
            
            String path = shortenedUrl.getPath().substring(1); // skip slash
            assertTrue(storageSet.add(path));
        }
    }
    
    @Test
    @DisplayName("Already shortened url shouldn't be accepted as an input")
    void urlIsNotShortened() {
        assertThrows(OwnUrlException.class, () -> urlShortener.shortenUrl("https://sh.rt/zxc1v"));    
    }
}