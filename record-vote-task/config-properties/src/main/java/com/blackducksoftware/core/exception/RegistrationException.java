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

public class RegistrationException extends BlackDuckSoftwareException {

    public RegistrationException(MessageValue messageValue) {
        super(messageValue);
    }
}
