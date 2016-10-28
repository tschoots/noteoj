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
package com.blackducksoftware.core.message.spring;

import org.springframework.context.MessageSource;
import org.springframework.context.support.DelegatingMessageSource;
import org.springframework.util.Assert;

/**
 * helper method to given an injected msg source to find our base one. In dependent contexts like the one in springmvc
 * the msg source will be a DeletagingMessageSource, with somewhere as parent the msg source of the root container,
 * which should be a {@code CoreResourceBundleMessageSource}
 * 
 * @author ydeboeck
 * 
 */
public final class CoreResourceBundleExtension {

    private final CoreResourceBundleMessageSource coreMessageSource;

    public CoreResourceBundleExtension(MessageSource messageSource) {
        coreMessageSource = init(messageSource);
    }

    private CoreResourceBundleMessageSource init(MessageSource messageSource) {
        MessageSource msgSrc = messageSource;
        while (msgSrc instanceof DelegatingMessageSource) {
            msgSrc = ((DelegatingMessageSource) msgSrc).getParentMessageSource();
        }

        if (msgSrc instanceof CoreResourceBundleMessageSource) {
            return (CoreResourceBundleMessageSource) msgSrc;
        }
        throw new IllegalStateException(
                "CoreResourceBundleExtension could not find underlying implemention of CoreResourceBundleMessageSource in MessageSource chain. MessageSource:"
                        + messageSource);
    }

    public void addBaseName(String name) {
        Assert.notNull(coreMessageSource, "MessageSource required - use alternate constructor: CoreResourceBundleExtension(MessageSource)");
        coreMessageSource.addBaseName(name);
    }
}
