package com.blackducksoftware.core.properties.configuration.spring;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blackducksoftware.core.properties.IPropertyConfigurationFactory;
import com.blackducksoftware.core.properties.configuration.PropertyConfiguration;
import com.blackducksoftware.core.properties.configuration.PropertyConfigurationContainer;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

public class SpringPropertyConfigurationFactory implements IPropertyConfigurationFactory {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final SpringEnvironmentManager springEnvironmentManager;

    private final Optional<SpringPropertyConfiguration> optionalSpringPropertyConfiguration;

    public SpringPropertyConfigurationFactory(SpringEnvironmentManager springEnvironmentManager) {
        this(null, springEnvironmentManager);
    }

    public SpringPropertyConfigurationFactory(SpringPropertyConfiguration springPropertyConfiguration, SpringEnvironmentManager springEnvironmentManager) {
        Preconditions.checkArgument(springEnvironmentManager != null, "Spring environment manager must be initialized.");

        this.springEnvironmentManager = springEnvironmentManager;
        optionalSpringPropertyConfiguration = Optional.fromNullable(springPropertyConfiguration);
    }

    @Override
    public Optional<PropertyConfiguration> create(String configurationName) {
        Preconditions.checkArgument(StringUtils.isNotBlank(configurationName), "Configuration name must not be blank.");

        logger.info("Attempting to create Spring property configuration.");

        SpringPropertyConfiguration springPropertyConfiguration = null;
        if (optionalSpringPropertyConfiguration.isPresent()) {
            springPropertyConfiguration = optionalSpringPropertyConfiguration.get();
        } else {
            springPropertyConfiguration = new SpringPropertyConfiguration(springEnvironmentManager);
        }

        PropertyConfiguration propertyConfiguration = new PropertyConfigurationContainer(springPropertyConfiguration, configurationName);

        logger.info("Successfully created Spring property configuration.");

        return Optional.of(propertyConfiguration);
    }
}
