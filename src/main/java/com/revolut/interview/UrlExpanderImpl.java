package com.revolut.interview;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;

import java.net.URL;

@AllArgsConstructor
public class UrlExpanderImpl implements UrlExpander {
    
    private static final String HOST = "sh.rt";
    public static final int PATH_LENGTH = 6;

    private final UrlSaver urlSaver;

    @Override
    @SneakyThrows
    public URL expendUrl(String shortUrl) {
        
        var inputUrl = new URL(shortUrl);

        validateHost(inputUrl.getHost());
        validatePath(inputUrl.getPath());

        var key = inputUrl.getPath().substring(1);// skip slash
        validateKey(key);

        return urlSaver.retrieve(key);
    }

    @SneakyThrows(UrlMismatchException.class)
    private static void validateHost(String input) {
        if (!UrlExpanderImpl.HOST.equals(input)) {
            throw new UrlMismatchException();
        }
    }
    
    @SneakyThrows(UrlMismatchException.class)
    private static void validatePath(String path) {
        if (path == null || path.length() != PATH_LENGTH) {
            throw new UrlMismatchException();
        }
    }
    
    @SneakyThrows(UrlMismatchException.class)
    private static void validateKey(String key) {
        if (!StringUtils.isAlphanumeric(key)) {
            throw new UrlMismatchException();
        }
    }
}