package com.blackducksoftware.core.properties;

import com.blackducksoftware.core.properties.configuration.PropertyConfiguration;
import com.google.common.base.Optional;

public interface IPropertyConfigurationFactory {
    /**
     * Creates a property configuration.
     * 
     * @param configurationName
     *            The configuration name. Must not be blank.
     * @return Optional<PropertyConfiguration> Returns the property configuration if it can be created and absence
     *         otherwise.
     */
    Optional<PropertyConfiguration> create(String configurationName);
}
