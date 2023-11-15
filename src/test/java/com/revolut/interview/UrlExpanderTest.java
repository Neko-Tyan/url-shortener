package com.revolut.interview;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class UrlExpanderTest {

    static UrlExpander urlExpander;

    public static Stream<String> getValidInput() {
        return Stream.of("https://sh.rt/dv56x");
    }

    public static Stream<String> getInvalidInput() {
        return Stream.of("whatever", "google.com", "1234", "", " ", null);
    }
    
    public static Stream<String> getWrongHostInput() {
        return Stream.of("https://www.google.com/search?q=stackoverflow",
                "https://www.weather.com", 
                "https://www.example.com:4343/path/");
    }

    public static Stream<String> getWrongPathInput() {
        return Stream.of("https://sh.rt/dv5",
                "https://sh.rt/dv56xrt9", 
                "https://sh.rt/",
                "https://sh.rt");
    }
    
    @BeforeAll
    @SneakyThrows
    static void setUp(){
        urlExpander = new UrlExpanderImpl(new UrlSaverImpl(Map.of("dv56x", new URL("https://weather.com"))));    
    }
    
    @ParameterizedTest
    @MethodSource("getValidInput")
    @DisplayName("Should return not null result")
    void notNullResult(String input) {
        var expendedUrl = urlExpander.expendUrl(input);
        
        assertNotNull(expendedUrl);
    }

    @ParameterizedTest
    @MethodSource("getValidInput")
    @DisplayName("Result shouldn't be equal to the input")
    void resultDiffersFromInput(String input) {

        var expendedUrl = urlExpander.expendUrl(input);
        
        assertNotEquals(input, expendedUrl.toString());
    }
    
    @ParameterizedTest
    @MethodSource("getInvalidInput")
    @DisplayName("Only valid URL input should be accepted")
    void exceptionOnInvalidUrl(String input) {
        assertThrows(MalformedURLException.class, () -> urlExpander.expendUrl(input));
    }
    
    @ParameterizedTest
    @MethodSource("getWrongHostInput")
    @DisplayName("Only correct host should be accepted")
    void exceptionOnForeignUrl(String input) {
        assertThrows(UrlMismatchException.class, () -> urlExpander.expendUrl(input));
    }
    
    @ParameterizedTest
    @MethodSource("getWrongPathInput")
    @DisplayName("Only shortener's URL with correct path should be accepted")
    void exceptionOnPartialOwnUrl(String input) {
        assertThrows(UrlMismatchException.class, () -> urlExpander.expendUrl(input));
    }
}