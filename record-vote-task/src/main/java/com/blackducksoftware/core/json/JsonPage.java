package com.blackducksoftware.core.json;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.blackducksoftware.core.exception.BlackDuckServerException;
//import com.blackducksoftware.core.page.Page;
//import com.blackducksoftware.core.page.ResourcePage;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class JsonPage {

    private static final ObjectMapper objectMapper = JsonUtil.configuredObjectMapper();

    private JsonPage() {
    }
/*
    public static <T> Page<T> readPage(String json, Class<? extends T> clazz) {
        try {
            JavaType pageType = objectMapper.getTypeFactory().constructParametricType(Page.class, clazz);
            Page<T> page = objectMapper.readValue(json, pageType);
            return page;
        } catch (IOException ex) {
            // TODO Exception handling
            throw new BlackDuckServerException("TODO Could not read JSON: " + ex.getMessage(), ex);
        }
    }

    public static <T> ResourcePage<T> readResourcePage(String json, Class<? extends T> clazz) {
        try {
            JavaType resourcePageType = objectMapper.getTypeFactory().constructParametricType(ResourcePage.class, clazz);
            ResourcePage<T> resourcePage = objectMapper.readValue(json, resourcePageType);
            return resourcePage;
        } catch (IOException ex) {
            // TODO Exception handling
            throw new BlackDuckServerException("TODO Could not read JSON: " + ex.getMessage(), ex);
        }
    }
*/
    public static <T> T readObject(String json, TypeReference<T> typeReference) {
        try {
            return objectMapper.readValue(json, typeReference);
        } catch (IOException ex) {
            // TODO Exception handling
            throw new BlackDuckServerException("TODO Could not read JSON: " + ex.getMessage(), ex);
        }
    }

    public static <T> List<T> readList(String json, Class<? extends T> clazz) {
        try {
            JavaType pageType = objectMapper.getTypeFactory().constructParametricType(List.class, clazz);
            List<T> page = objectMapper.readValue(json, pageType);
            return page;
        } catch (IOException ex) {
            // TODO Exception handling
            throw new BlackDuckServerException("TODO Could not read JSON: " + ex.getMessage(), ex);
        }
    }

    public static <K, V> Map<K, V> readMap(String json, Class<? extends K> keyClazz, Class<? extends V> valueClazz) {
        try {
            JavaType mapType = objectMapper.getTypeFactory().constructParametricType(Map.class, keyClazz, valueClazz);
            Map<K, V> map = objectMapper.readValue(json, mapType);
            return map;
        } catch (IOException ex) {
            // TODO Exception handling
            throw new BlackDuckServerException("TODO Could not read JSON: " + ex.getMessage(), ex);
        }
    }

}
