package com.blackducksoftware.core.message;

import java.util.Collections;
import java.util.Map;

import org.apache.commons.collections.MapUtils;

/**
 * {@link MessageValue} captures a translated i18n message. It has in addition to the translated message, also the full
 * context of the message: argument values and errorCode
 * 
 * @author ydeboeck
 * 
 */
public class MessageValue {

    private final String message;

    private final MessageDefinition definition;

    private final Map<String, ? extends Object> arguments;

    public MessageValue(String message, MessageDefinition definition, Map<String, ? extends Object> arguments) {
        this.message = message;
        this.definition = definition;
        this.arguments = MapUtils.isNotEmpty(arguments) ? arguments : Collections.EMPTY_MAP;
    }

    public String getMessage() {
        return message;
    }

    public MessageDefinition getDefinition() {
        return definition;
    }

    public Map<String, ? extends Object> getArguments() {
        return arguments;
    }

    public Object getArgument(String key) {
        return arguments.get(key);
    }

    /**
     * @param key
     * @param targetValueType
     * @return
     * @throws IllegalArgumentException
     *             when value is present but not of targetValueType
     */
    public <T> T getArgument(String key, Class<T> targetValueType) {
        Object v = arguments.get(key);
        if (v == null) {
            return null;
        }
        if (targetValueType.isInstance(v)) {
            return (T) v;
        }
        throw new IllegalArgumentException(
                String.format("Cannot convert value [%s] from source type [%s] to target type [%s]",
                        v, v.getClass().getSimpleName(), targetValueType.getSimpleName()));
    }

    /**
     * TODO consider deprecate
     * msg to support old-style code/msg
     * 
     * @param code
     * @param message
     * @return
     */
    public static MessageValue valueOf(String code, String message) {
        MessageDefinition d = new MessageDefinition(code);
        MessageValue v = new MessageValue(message, d, null);
        return v;
    }
}
