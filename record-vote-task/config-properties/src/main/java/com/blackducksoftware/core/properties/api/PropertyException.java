package com.blackducksoftware.core.properties.api;

import com.blackducksoftware.core.exception.BlackDuckSoftwareException;
import com.blackducksoftware.core.message.MessageValue;
import com.google.common.base.Optional;

public class PropertyException extends BlackDuckSoftwareException {
    private final Optional<String> propertyName;

    public PropertyException(MessageValue messageValue) {
        this(messageValue, null, null);
    }

    public PropertyException(MessageValue messageValue, Throwable cause) {
        this(messageValue, cause, null);
    }

    public PropertyException(MessageValue messageValue, String propertyName) {
        this(messageValue, null, propertyName);

    }

    public PropertyException(MessageValue messageValue, Throwable cause, String propertyName) {
        super(messageValue, cause);

        this.propertyName = Optional.fromNullable(propertyName);
    }

    public Optional<String> getPropertyName() {
        return propertyName;
    }
}
