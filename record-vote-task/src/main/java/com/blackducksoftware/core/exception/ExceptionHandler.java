/*
 * Copyright (C) 2009 Black Duck Software Inc.
 * http://www.blackducksoftware.com/
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * Black Duck Software ("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Black Duck Software.
 */
package com.blackducksoftware.core.exception;

import com.blackducksoftware.core.message.IMessageLookup;
import com.blackducksoftware.core.message.MessageDefinition;
import com.blackducksoftware.core.message.MessageValue;
import com.google.common.base.Preconditions;

/**
 * Exception handler.
 *
 * @author ydeboeck
 */
public class ExceptionHandler {
    private final IMessageLookup messageLookup;

    public ExceptionHandler(IMessageLookup messageLookup) {
        this.messageLookup = Preconditions.checkNotNull(messageLookup, "Message lookup must be initialized.");
    }

    public BlackDuckSoftwareException createBlackDuckSoftwareException(MessageDefinition definition, Object... args) {
        return createBlackDuckSoftwareException(null, definition, args);
    }

    public BlackDuckSoftwareException createBlackDuckSoftwareException(Exception cause, MessageDefinition definition, Object... args) {
        MessageValue value = messageLookup.lookup(definition, args);
        return new BlackDuckSoftwareException(value, cause);
    }

    public ConfigurationException createConfigurationException(MessageDefinition definition, Object... args) {
        return createConfigurationException(null, definition, args);
    }

    public ConfigurationException createConfigurationException(Exception cause, MessageDefinition definition, Object... args) {
        MessageValue value = messageLookup.lookup(definition, args);
        return new ConfigurationException(value, cause);
    }

    public AuthorizationException createAuthorizationException(String message) {
        return new AuthorizationException(message);
    }
}
