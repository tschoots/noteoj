package com.blackducksoftware.core.properties.configuration;

import org.apache.commons.configuration.AbstractConfiguration;

public interface PropertyConfiguration {
    /**
     * Gets the abstract configuration.
     * 
     * @return AbstractConfiguration Returns the abstract configuration.
     */
    AbstractConfiguration getAbstractConfiguration();

    /**
     * Gets the configuration name.
     * 
     * @return String Returns the configuration name.
     */
    String getConfigurationName();
}
