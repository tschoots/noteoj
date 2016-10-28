/*
 * Copyright (C) 2011 Black Duck Software Inc.
 * http://www.blackducksoftware.com/
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * Black Duck Software ("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Black Duck Software.
 */
package com.blackducksoftware.core.message;

import java.util.Locale;

import org.springframework.context.i18n.LocaleContextHolder;

/**
 * abstraction of spring's MessageSource
 *
 * looks up the locale from {@link LocaleContextHolder}
 *
 * @author ydeboeck
 *
 */
public interface IMessageLookup {
    @Deprecated
    String lookup(String msgCode, Object... args);

    /**
     * Look up a message value by a message definition and zero or more message arguments.
     *
     * @param messageDefinition
     *            The message definition. Must be initialized.
     * @param arguments
     *            The arguments.
     * @return MessageValue Returns the message value.
     */
    MessageValue lookup(MessageDefinition messageDefinition, Object... arguments);

    /**
     * Look up a message value by a message definition and zero or more message arguments overriding the contextual
     * locale with the given locale.
     *
     * @param locale
     *            The locale.
     * @param messageDefinition
     *            The message definition. Must be initialized.
     * @param arguments
     *            The arguments.
     * @return MessageValue Returns the message value.
     */
    MessageValue lookup(Locale locale, MessageDefinition messageDefinition, Object... arguments);
}
