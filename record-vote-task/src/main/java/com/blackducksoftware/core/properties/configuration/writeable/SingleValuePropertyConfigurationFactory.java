package com.blackducksoftware.core.properties.configuration.writeable;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blackducksoftware.core.properties.IPropertyConfigurationFactory;
import com.blackducksoftware.core.properties.configuration.PropertyConfiguration;
import com.blackducksoftware.core.properties.configuration.PropertyConfigurationContainer;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.netflix.config.DynamicWatchedConfiguration;

public class SingleValuePropertyConfigurationFactory implements IPropertyConfigurationFactory {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final String propertyName;

    private final Optional<SingleValueConfigurationSource> optionalSingleValueConfigurationSource;

    public SingleValuePropertyConfigurationFactory(String propertyName) {
        Preconditions.checkArgument(StringUtils.isNotBlank(propertyName), "Property name must not be blank.");

        optionalSingleValueConfigurationSource = Optional.absent();
        this.propertyName = propertyName;
    }

    public SingleValuePropertyConfigurationFactory(SingleValueConfigurationSource singleValueConfigurationSource) {
        Preconditions.checkArgument(singleValueConfigurationSource != null, "Single value configuration source must be initialized.");

        optionalSingleValueConfigurationSource = Optional.of(singleValueConfigurationSource);
        propertyName = singleValueConfigurationSource.getPropertyName();
    }

    @Override
    public Optional<PropertyConfiguration> create(String configurationName) {
        Preconditions.checkArgument(StringUtils.isNotBlank(configurationName), "Configuration name must not be blank.");

        logger.info("Attempting to create single value property configuration.");

        SingleValueConfigurationSource singleValueConfigurationSource = null;
        if (optionalSingleValueConfigurationSource.isPresent()) {
            singleValueConfigurationSource = optionalSingleValueConfigurationSource.get();
        } else {
            singleValueConfigurationSource = new SingleValueConfigurationSource(propertyName);
        }

        DynamicWatchedConfiguration dynamicWatchedConfiguration = new DynamicWatchedConfiguration(singleValueConfigurationSource);

        PropertyConfiguration propertyConfiguration = new PropertyConfigurationContainer(dynamicWatchedConfiguration, configurationName);

        logger.info("Successfully created single value property configuration.");

        return Optional.of(propertyConfiguration);
    }
}
