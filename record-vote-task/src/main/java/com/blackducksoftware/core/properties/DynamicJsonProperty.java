package com.blackducksoftware.core.properties;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blackducksoftware.core.json.JsonUtil;
import com.blackducksoftware.core.properties.api.Property;
import com.blackducksoftware.core.properties.api.PropertyCallback;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import com.netflix.config.PropertyWrapper;

public class DynamicJsonProperty<T> implements Property<T> {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final PropertyWrapper<String> propertyWrapper;

    private final T defaultValue;

    private final Class<T> targetClass;

    private final ObjectMapper objectMapper;

    public DynamicJsonProperty(PropertyWrapper<String> propertyWrapper, T defaultValue, Class<T> targetClass) {
        Preconditions.checkArgument(propertyWrapper != null, "Property wrapper must be initialized.");
        Preconditions.checkArgument(targetClass != null, "Target class must be initialized.");

        this.propertyWrapper = propertyWrapper;
        this.defaultValue = defaultValue;
        this.targetClass = targetClass;

        objectMapper = JsonUtil.configuredObjectMapper();
    }

    @Override
    public String getName() {
        return propertyWrapper.getName();
    }

    @Override
    public T getValue() {
        T actualValue = null;

        String value = propertyWrapper.getValue();
        if (value != null) {
            // Value is defined, attempt to de-serialize it.
            try {
                actualValue = objectMapper.readValue(value, targetClass);
            } catch (IOException e) {
                // Unable to deserialize JSON, return the default value.
                String name = getName();
                logger.debug("Unable to convert JSON property value to object [Name: {} | Value: {}].", name, value, e);
                logger.warn("Unable to convert JSON property value to object [Name: {} | Value: {}]: {}", name, value, e.getMessage());
                actualValue = defaultValue;
            }
        } else {
            // Value is not defined, return the default value
            actualValue = defaultValue;
        }

        return actualValue;
    }

    @Override
    public void addCallback(PropertyCallback propertyCallback) { // NOPMD DoNotUseThreads Archaius
        Preconditions.checkArgument(propertyCallback != null, "Property callback must be initialized.");

        propertyWrapper.addCallback(propertyCallback);
    }

    @Override
    public boolean removeCallback(PropertyCallback propertyCallback) { // NOPMD DoNotUseThreads Archaius
        Preconditions.checkArgument(propertyCallback != null, "Property callback must be initialized.");

        boolean isRemoved = false;

        com.netflix.config.DynamicProperty dynamicProperty = propertyWrapper.getDynamicProperty();
        if (dynamicProperty != null) {
            isRemoved = dynamicProperty.removeCallback(propertyCallback);
        }

        return isRemoved;
    }

    @Override
    public T getDefaultValue() {
        return defaultValue;
    }
}
