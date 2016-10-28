package com.blackducksoftware.core.exception;

import com.blackducksoftware.core.message.MessageValue;

/**
 * a configuration exception relates to modules/extension points/ plugins/extensions
 * 
 * @author ydeboeck
 * 
 */
public class ConfigurationException extends BlackDuckSoftwareException {

    public ConfigurationException(MessageValue messageValue) {
        super(messageValue);
    }

    public ConfigurationException(MessageValue messageValue, Exception cause) {
        super(messageValue, cause);
    }

}
