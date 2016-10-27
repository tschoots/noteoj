package com.blackducksoftware.core.properties;

import com.blackducksoftware.core.message.MessageDefinition;

public final class PropertyMessages {
    // Arguments

    public static final String ARGUMENT_PROPERTY_NAME = "propertyName";

    public static final String ARGUMENT_PROPERTY_VALUE = "propertyValue";

    public static final String ARGUMENT_PROPERTY_CLASS = "propertyClass";

    public static final String ARGUMENT_CONNECTION_STRING = "connectionString";

    public static final String ARGUMENT_CONFIG_ROOT_PATH = "configRootPath";

    public static final String ARGUMENT_ERROR_MESSAGE = "errorMessage";

    // Messages

    public static final String REQUIRED_PROPERTY_DOES_NOT_EXIST_CODE = "{core.properties.required.property.does.not.exist}";

    public static final MessageDefinition REQUIRED_PROPERTY_DOES_NOT_EXIST = new MessageDefinition(REQUIRED_PROPERTY_DOES_NOT_EXIST_CODE,
            ARGUMENT_PROPERTY_NAME);

    public static final String REQUIRED_PROPERTY_DOES_NOT_CONVERT_CODE = "{core.properties.required.property.does.not.convert}";

    public static final MessageDefinition REQUIRED_PROPERTY_DOES_NOT_CONVERT = new MessageDefinition(REQUIRED_PROPERTY_DOES_NOT_CONVERT_CODE,
            ARGUMENT_PROPERTY_NAME, ARGUMENT_PROPERTY_CLASS);

    public static final String CALLBACK_PROPERTY_DOES_NOT_EXIST_CODE = "{core.properties.callback.property.does.not.exist}";

    public static final MessageDefinition CALLBACK_PROPERTY_DOES_NOT_EXIST = new MessageDefinition(CALLBACK_PROPERTY_DOES_NOT_EXIST_CODE,
            ARGUMENT_PROPERTY_NAME);

    public static final String ZOOKEEPER_CONFIGURATION_SOURCE_CANNOT_START_CODE = "{core.properties.zookeeper.configuration.source.cannot.start}";

    public static final MessageDefinition ZOOKEEPER_CONFIGURATION_SOURCE_CANNOT_START = new MessageDefinition(
            ZOOKEEPER_CONFIGURATION_SOURCE_CANNOT_START_CODE, ARGUMENT_CONNECTION_STRING, ARGUMENT_CONFIG_ROOT_PATH);

    public static final String ZOOKEEPER_CREATE_PROPERTY_FAILED_CODE = "{core.properties.zookeeper.create.property.failed}";

    public static final MessageDefinition ZOOKEEPER_CREATE_PROPERTY_FAILED = new MessageDefinition(ZOOKEEPER_CREATE_PROPERTY_FAILED_CODE,
            ARGUMENT_PROPERTY_NAME, ARGUMENT_PROPERTY_VALUE, ARGUMENT_ERROR_MESSAGE);

    public static final String ZOOKEEPER_UPDATE_PROPERTY_FAILED_CODE = "{core.properties.zookeeper.update.property.failed}";

    public static final MessageDefinition ZOOKEEPER_UPDATE_PROPERTY_FAILED = new MessageDefinition(ZOOKEEPER_UPDATE_PROPERTY_FAILED_CODE,
            ARGUMENT_PROPERTY_NAME, ARGUMENT_PROPERTY_VALUE, ARGUMENT_ERROR_MESSAGE);

    public static final String ZOOKEEPER_DELETE_PROPERTY_FAILED_CODE = "{core.properties.zookeeper.delete.property.failed}";

    public static final MessageDefinition ZOOKEEPER_DELETE_PROPERTY_FAILED = new MessageDefinition(ZOOKEEPER_DELETE_PROPERTY_FAILED_CODE,
            ARGUMENT_PROPERTY_NAME, ARGUMENT_ERROR_MESSAGE);

    private PropertyMessages() {
    }
}
