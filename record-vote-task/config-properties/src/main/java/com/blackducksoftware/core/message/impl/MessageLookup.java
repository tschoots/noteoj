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
package com.blackducksoftware.core.message.impl;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.validation.MessageInterpolator;
import javax.validation.MessageInterpolator.Context;
import javax.validation.metadata.ConstraintDescriptor;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import com.blackducksoftware.core.message.IMessageLookup;
import com.blackducksoftware.core.message.MessageDefinition;
import com.blackducksoftware.core.message.MessageValue;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

/**
 * TODO make user locale specific
 *
 * @author ydeboeck
 *
 */
public class MessageLookup implements IMessageLookup {
    /**
     * this is a {@link Context} to be able to work with {@link MessageInterpolator} it only
     * exposes arguments to build messages
     *
     * @author ydeboeck
     *
     */
    private static final class MessageContext implements Context {

        private final Map<String, Object> attributes = Maps.newHashMap();

        private final ArgConstraintDescriptor desciptor = new ArgConstraintDescriptor(attributes);

        @Override
        public ConstraintDescriptor<?> getConstraintDescriptor() {
            return desciptor;
        }

        @Override
        public Object getValidatedValue() {
            return null;
        }

        public Map<String, Object> getAttributes() {
            return attributes;
        }

        public void addAttribute(String key, Object value) {
            attributes.put(key, value);
        }
    }

    /**
     * this is just an no-op ConstraintDescriptor to be able to work with the {@link MessageInterpolator} it only
     * exposes the arguments it was constructed with
     *
     * @author ydeboeck
     *
     */
    private static final class ArgConstraintDescriptor implements ConstraintDescriptor<Annotation> {

        private final Map<String, Object> attributes;

        public ArgConstraintDescriptor(Map<String, Object> attributes) {
            this.attributes = attributes;
        }

        @Override
        public Annotation getAnnotation() {
            return null;
        }

        @Override
        public Set getGroups() {
            return null;
        }

        @Override
        public Set getPayload() {
            return null;
        }

        @Override
        public List getConstraintValidatorClasses() {
            return null;
        }

        @Override
        public Map getAttributes() {
            return attributes;
        }

        @Override
        public Set getComposingConstraints() {
            return null;
        }

        @Override
        public boolean isReportAsSingleViolation() {
            return false;
        }
    }

    private final MessageSource msgSource;

    private final MessageInterpolator interpolator;

    public MessageLookup(MessageInterpolator interpolator, MessageSource msgSource) {
        this.interpolator = interpolator;
        this.msgSource = msgSource;
    }

    @Override
    public String lookup(String msgCode, Object... args) {
        String message = msgSource.getMessage(msgCode, args, LocaleContextHolder.getLocale());
        return message;
    }

    @Override
    public MessageValue lookup(MessageDefinition messageDefinition, Object... arguments) {
        checkPreconditions(messageDefinition, arguments);

        return constructMessageValue(messageDefinition, arguments);
    }

    @Override
    public MessageValue lookup(Locale locale, MessageDefinition messageDefinition, Object... arguments) {
        Preconditions.checkNotNull(locale, "Locale must be initialized.");
        checkPreconditions(messageDefinition, arguments);

        MessageValue messageValue = null;

        Locale originalLocale = LocaleContextHolder.getLocale();
        try {
            LocaleContextHolder.setLocale(locale);

            messageValue = constructMessageValue(messageDefinition, arguments);
        } finally {
            LocaleContextHolder.setLocale(originalLocale);
        }

        return messageValue;
    }

    private void checkPreconditions(MessageDefinition messageDefinition, Object... arguments) {
        Preconditions.checkNotNull(messageDefinition, "Message definition must be initialized.");

        int numberOfArgumentNames = messageDefinition.getNumberOfArgumentNames();
        int numberOfProvidedArguments = (arguments != null) ? arguments.length : 0;
        Preconditions.checkArgument(numberOfArgumentNames == numberOfProvidedArguments,
                "Number of message definition arguments (" + numberOfArgumentNames + ") must equal the number of provided arguments ("
                        + numberOfProvidedArguments + ").");
    }

    private MessageValue constructMessageValue(MessageDefinition messageDefinition, Object... arguments) {
        MessageContext messageContext = new MessageContext();
        String[] argumentNames = messageDefinition.getArgumentNames();
        int numberOfArgumentNames = messageDefinition.getNumberOfArgumentNames();
        for (int i = 0; i < numberOfArgumentNames; i++) {
            messageContext.addAttribute(argumentNames[i], arguments[i]);
        }

        String code = messageDefinition.getCode();
        String message = interpolator.interpolate(code, messageContext);

        Map<String, Object> attributes = messageContext.getAttributes();

        return new MessageValue(message, messageDefinition, attributes);
    }
}
