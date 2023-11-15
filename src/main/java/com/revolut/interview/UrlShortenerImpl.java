package com.revolut.interview;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import java.net.URL;

@AllArgsConstructor
public class UrlShortenerImpl implements UrlShortener {
    
    private static final String PROTOCOL = "https";
    private static final String HOST = "sh.rt";
    
    private final UrlSaver urlSaver;

    @Override
    @SneakyThrows
    public URL shortenUrl(String longUrl) {
        
        var value = new URL(longUrl);
        
        validateHost(value);
        
        String key;
        boolean isSaved;
        
        do {
            key = RandomStringUtils.randomAlphanumeric(5);
            isSaved = urlSaver.save(key, value);
        } while (!isSaved);

        return new URL(PROTOCOL, HOST, StringUtils.join("/", key));
    }

    @SneakyThrows(OwnUrlException.class)
    private static void validateHost(URL input) {
        if (HOST.equals(input.getHost())) {
            throw new OwnUrlException();
        }
    }
}