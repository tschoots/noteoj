package com.blackducksoftware.core.json;

import java.io.IOException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blackducksoftware.core.exception.BlackDuckServerException;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.google.common.base.Optional;

/**
 *
 * @author ydeboeck
 *
 */
public final class JsonUtil {
    public static final ObjectMapper OBJECT_MAPPER = JsonUtil.configuredObjectMapper();

    private static final Logger logger = LoggerFactory.getLogger(JsonUtil.class);

    private JsonUtil() {
    }

    public static String writeValueAsString(final Object value) {
        try {
            return OBJECT_MAPPER.writeValueAsString(value);
        } catch (final JsonProcessingException e) {
            throw new BlackDuckServerException("Unable to serialize value to JSON: " + value, e);
        }
    }

    public static <T> T readValue(final String json, final Class<T> clazz) {
        try {
            return OBJECT_MAPPER.readValue(json, clazz);
        } catch (final IOException e) {
            throw new BlackDuckServerException("Unable to deserialize JSON to value: " + json, e);
        }
    }

    /**
     * creates a new ObjectMapper and configures it.
     *
     * @return
     */
    public static ObjectMapper configuredObjectMapper() {
        return configureJacksonJson(new ObjectMapper());
    }

    /**
     * configure common jackson settings like fail on unknown props
     *
     * @param objectMapper
     * @return
     */
    public static ObjectMapper configureJacksonJson(ObjectMapper objectMapper) {

        // Instead of serializing to milliseconds, print out formatted UTC string
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        // Convert Date and DateTimes to UTC
        objectMapper.configure(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE, true);

        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        // TWOM-5741 upwards compatibility for enums, see EnumJacksonTest for examples
        objectMapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);

        objectMapper.registerModule(new JodaModule());
        objectMapper.registerModule(new GuavaModule());

        // Shouldn't need this if we are only using DateTime, but this is a fallback
        // in case we need to de/serialize java.util.Date objects
        objectMapper.registerModule(getJavaUtilDateModule());

        objectMapper.setSerializationInclusion(Include.NON_NULL);

        return objectMapper;
    }

    public static <T> Optional<T> readValueGracefully(final String json, final Class<T> clazz) {
        T value = null;

        try {
            value = OBJECT_MAPPER.readValue(json, clazz);
        } catch (final IOException e) {
            // Log, but do nothing.
            logger.warn("Unable to deserialize JSON value to object [JSON: {} | Class: {}]: {}", json, clazz, e.getMessage());
            logger.debug("Unable to deserialize JSON value to object [JSON: {} | Class: {}]", json, clazz, e);
        }

        return Optional.fromNullable(value);
    }

    private static SimpleModule getJavaUtilDateModule() {
        SimpleModule javaUtilDateModule = new SimpleModule("JavaUtilDateModule");
        javaUtilDateModule.addSerializer(Date.class, new JavaUtilDateSerializer());
        javaUtilDateModule.addDeserializer(Date.class, new JavaUtilDateDeserializer());
        return javaUtilDateModule;
    }
}
