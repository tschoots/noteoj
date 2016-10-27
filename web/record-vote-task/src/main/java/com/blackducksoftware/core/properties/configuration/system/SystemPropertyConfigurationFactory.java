package com.blackducksoftware.core.properties.configuration.system;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blackducksoftware.core.properties.IPropertyConfigurationFactory;
import com.blackducksoftware.core.properties.api.PropertyConstants;
import com.blackducksoftware.core.properties.configuration.PropertyConfiguration;
import com.blackducksoftware.core.properties.configuration.PropertyConfigurationContainer;
import com.blackducksoftware.core.properties.configuration.PropertyPollingScheduler;
import com.blackducksoftware.core.properties.configuration.spring.SpringEnvironmentManager;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.netflix.config.AbstractPollingScheduler;
import com.netflix.config.DynamicConfiguration;
import com.netflix.config.PolledConfigurationSource;

public class SystemPropertyConfigurationFactory implements IPropertyConfigurationFactory {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final String pollerName;

    private final SpringEnvironmentManager springEnvironmentManager;

    public SystemPropertyConfigurationFactory(String pollerName, SpringEnvironmentManager springEnvironmentManager) {
        Preconditions.checkArgument(StringUtils.isNotBlank(pollerName), "Poller name must not be blank.");
        Preconditions.checkArgument(springEnvironmentManager != null, "Spring environment manager must be initialized.");

        this.pollerName = pollerName;
        this.springEnvironmentManager = springEnvironmentManager;
    }

    @Override
    public Optional<PropertyConfiguration> create(String configurationName) {
        Preconditions.checkArgument(StringUtils.isNotBlank(configurationName), "Configuration name must not be blank.");

        logger.info("Attempting to create dynamic system property configuration.");

        final String initialDelayMillisString = springEnvironmentManager.getProperty(PropertyConstants.POLLING_INITIAL_DELAY);
        final String delayMillisString = springEnvironmentManager.getProperty(PropertyConstants.POLLING_DELAY);
        final Optional<Long> optionalInitialDelayMillis = convertToLong(initialDelayMillisString);
        final Optional<Long> optionalDelayMillis = convertToLong(delayMillisString);

        AbstractPollingScheduler pollingScheduler = null;
        if ((optionalInitialDelayMillis.isPresent()) && (optionalDelayMillis.isPresent())) {
            // Use configured timers.
            long initialDelayMillis = optionalInitialDelayMillis.get();
            long delayMillis = optionalDelayMillis.get();
            pollingScheduler = new PropertyPollingScheduler(pollerName, initialDelayMillis, delayMillis);
        } else {
            // Use default timers.
            pollingScheduler = new PropertyPollingScheduler(pollerName);
        }

        PolledConfigurationSource polledConfigurationSource = new SystemPropertyPolledConfigurationSource();
        DynamicConfiguration dynamicConfiguration = new DynamicConfiguration(polledConfigurationSource, pollingScheduler);
        PropertyConfiguration propertyConfiguration = new PropertyConfigurationContainer(dynamicConfiguration, configurationName);

        logger.info("Successfully created dynamic system property configuration.");

        return Optional.of(propertyConfiguration);
    }

    private Optional<Long> convertToLong(String value) {
        Long result = null;

        if (value != null) {
            try {
                result = Long.valueOf(value);
            } catch (NumberFormatException e) {
                // Do nothing.
                logger.debug("Unable to convert value, '{}', to long.", value, e);
            }
        }

        return Optional.fromNullable(result);
    }
}
