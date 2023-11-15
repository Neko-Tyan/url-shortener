package com.revolut.interview;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReversibilityTest {

    UrlSaverImpl urlSaver = new UrlSaverImpl(new ConcurrentHashMap<>());
    
    UrlShortener urlShortener = new UrlShortenerImpl(urlSaver);
    UrlExpander urlExpander = new UrlExpanderImpl(urlSaver);

    public static Stream<String> getValidInput() {
        return Stream.of(
                "https://www.google.com/search?q=stackoverflow", 
                "https://weather.com/?q=SantiagoDeCampostela",
                "https://en.wikipedia.org/wiki/",
                "https://www.google.com/search?q=stackoverflow"
        );
    }

    @RepeatedTest(100)
    @DisplayName("Url should match original every time after shortening and expending")
    void matchWithOriginalAfterFullConversionRepeatable() {
        var originalUrl = "https://weather.com/?q=SantiagoDeCampostela";
        
        var shortenedUrl = urlShortener.shortenUrl(originalUrl);
        var expendedUrl = urlExpander.expendUrl(shortenedUrl.toString());

        assertEquals(originalUrl, expendedUrl.toString());
    }
    
    @ParameterizedTest
    @MethodSource("getValidInput")
    @DisplayName("Url should match original for any valid URL after shortening and expending")
    void matchWithOriginalAfterFullConversionVariable(String input) {
        var shortenedUrl = urlShortener.shortenUrl(input);
        var expendedUrl = urlExpander.expendUrl(shortenedUrl.toString());

        assertEquals(input, expendedUrl.toString());
    }
}