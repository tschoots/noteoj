package com.blackducksoftware.core.properties;

import com.blackducksoftware.core.message.IMessageLookup;
import com.blackducksoftware.core.message.MessageDefinition;
import com.blackducksoftware.core.message.MessageValue;
import com.blackducksoftware.core.properties.api.PropertyException;
import com.google.common.base.Preconditions;

public class PropertyExceptionHandler {
    private final IMessageLookup messageLookup;

    public PropertyExceptionHandler(IMessageLookup messageLookup) {
        Preconditions.checkArgument(messageLookup != null, "Message lookup must be initialized.");

        this.messageLookup = messageLookup;
    }

    public PropertyException createPropertyException(Throwable cause, MessageDefinition messageDefinition, Object... argumentArray) {
        MessageValue messageValue = messageLookup.lookup(messageDefinition, argumentArray);
        return new PropertyException(messageValue, cause);
    }

    public PropertyException createPropertyException(String propertyName, MessageDefinition messageDefinition, Object... argumentArray) {
        MessageValue messageValue = messageLookup.lookup(messageDefinition, argumentArray);
        return new PropertyException(messageValue, propertyName);
    }

    public PropertyException createPropertyException(Throwable cause, String propertyName, MessageDefinition messageDefinition, Object... argumentArray) {
        MessageValue messageValue = messageLookup.lookup(messageDefinition, argumentArray);
        return new PropertyException(messageValue, cause, propertyName);
    }
}
