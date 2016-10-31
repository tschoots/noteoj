package com.blackducksoftware.core.properties.configuration;

import java.util.Objects;

import org.apache.commons.configuration.AbstractConfiguration;
import org.apache.commons.lang3.StringUtils;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;

public class PropertyConfigurationContainer implements PropertyConfiguration {
    private final AbstractConfiguration abstractConfiguration;

    private final String configurationName;

    public PropertyConfigurationContainer(AbstractConfiguration abstractConfiguration, String configurationName) {
        Preconditions.checkArgument(abstractConfiguration != null, "Abstract configuration must be initialized.");
        Preconditions.checkArgument(StringUtils.isNotBlank(configurationName), "Configuration name must not be blank.");

        this.abstractConfiguration = abstractConfiguration;
        this.configurationName = configurationName;
    }

    @Override
    public AbstractConfiguration getAbstractConfiguration() {
        return abstractConfiguration;
    }

    @Override
    public String getConfigurationName() {
        return configurationName;
    }

    @Override
    public int hashCode() {
        return Objects.hash(abstractConfiguration, configurationName);
    }

    @Override
    public boolean equals(Object otherObject) {
        if (this == otherObject) {
            return true;
        }

        if ((null == otherObject) || (getClass() != otherObject.getClass())) {
            return false;
        }

        PropertyConfigurationContainer otherPropertyConfigurationContainer = (PropertyConfigurationContainer) otherObject;

        return Objects.equals(abstractConfiguration, otherPropertyConfigurationContainer.getAbstractConfiguration())
                && Objects.equals(configurationName, otherPropertyConfigurationContainer.getConfigurationName());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues()
                .add("Abstract configuration", abstractConfiguration)
                .add("Configuration name", configurationName).toString();
    }
}
