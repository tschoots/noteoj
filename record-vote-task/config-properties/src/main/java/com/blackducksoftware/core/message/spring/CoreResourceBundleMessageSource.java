/*
 * Copyright (C) 2012 Black Duck Software Inc.
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.context.support.ResourceBundleMessageSource;

/**
 * extension of spring's msg source to allow addition of properties file per module in multiple places of the spring
 * config
 * 
 * @author ydeboeck
 * 
 */
public class CoreResourceBundleMessageSource extends ResourceBundleMessageSource {

    private final List<String> basenames = new ArrayList<String>();

    public void addBaseName(String name) {
        basenames.add(name);
        super.setBasenames(basenames.toArray(ArrayUtils.EMPTY_STRING_ARRAY));
    }

    @Override
    public void setBasenames(String... basenames) {
        this.basenames.addAll(Arrays.asList(basenames));
        super.setBasenames(basenames);
    }

    public List<String> getBasenames() {
        return basenames;
    }

    @Override
    public ResourceBundle getResourceBundle(String basename, Locale locale) {
        return super.getResourceBundle(basename, locale);
    }
}
