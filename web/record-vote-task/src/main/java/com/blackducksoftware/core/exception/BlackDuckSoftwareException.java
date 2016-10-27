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

import com.blackducksoftware.core.message.MessageValue;
import com.google.common.base.Throwables;

// TODO rework with messageValue
public class BlackDuckSoftwareException extends RuntimeException {

    /**
     * has own message prop. super message prop can not be set outside constructor.
     * <p>
     * The goal was (is) to reconstruct this exception after serialization through e.g. SOAP/JSON, hence the setter.
     */
    private final String message;

    private final MessageValue messageValue;

    public BlackDuckSoftwareException(MessageValue messageValue) {
        super(messageValue.getMessage());
        message = messageValue.getMessage();
        this.messageValue = messageValue;
    }

    public BlackDuckSoftwareException(MessageValue messageValue, Throwable cause) {
        super(messageValue.getMessage(), cause);
        message = messageValue.getMessage();
        this.messageValue = messageValue;
    }

    protected BlackDuckSoftwareException(Throwable cause) {
        super(cause);
        message = null;
        messageValue = null;
    }

    /**
     * use BlackDuckServerException for exceptions that do not need i18n'd messages.
     * 
     * @param message
     */
    protected BlackDuckSoftwareException(String message) {
        super(message);
        this.message = message;
        messageValue = null;
    }

    /**
     * use BlackDuckServerException for exceptions that do not need i18n'd messages.
     * 
     * @param message
     */
    protected BlackDuckSoftwareException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
        messageValue = null;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public String getErrorCode() {
        return messageValue == null ? null : messageValue.getDefinition().getCode();
    }

    public MessageValue getMessageValue() {
        return messageValue;
    }

    /**
     * avoid class name as part of exception
     */
    @Override
    public String toString() {
        String msg = getLocalizedMessage();
        return (msg != null) ? msg : super.toString();
    }

    /**
     * Propagates {@code throwable} as-is if it is an instance of {@link BlackDuckSoftwareException} or else as
     * a last resort, wraps
     * it in a {@code BlackDuckSoftwareException} then propagates.
     * <p>
     * This method always throws an exception. The {@code BlackDuckSoftwareException} return type is only for client
     * code to make Java type system happy in case a return value is required by the enclosing method. Example usage:
     * 
     * <pre>
     * T doSomething() {
     *     try {
     *         return someMethodThatCouldThrowAnything();
     *     } catch (IKnowWhatToDoWithThisException e) {
     *         return handle(e);
     *     } catch (Throwable t) {
     *         throw BlackDuckSoftwareException.propagate(t);
     *     }
     * }
     * </pre>
     * 
     * @param throwable
     *            the Throwable to propagate
     * @return nothing will ever be returned; this return type is only for your
     *         convenience, as illustrated in the example above
     */
    public static BlackDuckSoftwareException propagate(Throwable throwable) {
        Throwables.propagateIfInstanceOf(throwable, BlackDuckSoftwareException.class);
        throw new BlackDuckServerException(throwable);
    }
}
