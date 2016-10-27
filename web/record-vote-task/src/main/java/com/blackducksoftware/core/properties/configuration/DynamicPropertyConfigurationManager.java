package com.blackducksoftware.core.properties.configuration;

import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.commons.configuration.AbstractConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.netflix.config.ConcurrentCompositeConfiguration;
import com.netflix.config.ConfigurationManager;

/**
 * Dynamic property manager. Responsible for defining, loading, and prioritizing property configurations.
 * 
 * This implementation is not thread-safe.
 * 
 * Highest priority to lowest priority.
 * 
 * 1. Node-specific Zookeeper properties
 * 2. Common Zookeeper properties
 * 3. Java system properties
 * 4. File-based properties
 * 5. Defaults - Not defined in a configuration, but the responsibility of the caller.
 * 
 * @author skatzman
 */
public final class DynamicPropertyConfigurationManager {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final ConcurrentCompositeConfiguration concurrentCompositeConfiguration;

    public DynamicPropertyConfigurationManager() {
        concurrentCompositeConfiguration = new ConcurrentCompositeConfiguration();

        initialize();
    }

    // Utility

    @PostConstruct
    public void install() {
        if (ConfigurationManager.isConfigurationInstalled()) {
            logger.warn("Unable to install dynamic property configuration because a configuration is already installed.");
        } else {
            logger.info("Attempting to install dynamic property configuration.");
            ConfigurationManager.install(concurrentCompositeConfiguration);
            logger.info("Successfully installed dynamic property configuration.");
        }
    }

    // Configurations

    public int numberOfConfigurations() {
        concurrentCompositeConfiguration.getConfigurations();

        return concurrentCompositeConfiguration.getConfigurations().size();
    }

    public boolean isConfigurationPresent(String configurationName) {
        Preconditions.checkArgument(StringUtils.isNotBlank(configurationName), "Configuration name must not be blank.");

        Configuration configuration = concurrentCompositeConfiguration.getConfiguration(configurationName);

        return (configuration != null);
    }

    public Set<String> getConfigurationNames() {
        return concurrentCompositeConfiguration.getConfigurationNames();
    }

    public List<AbstractConfiguration> getConfigurations() {
        return concurrentCompositeConfiguration.getConfigurations();
    }

    /**
     * Add a configuration to the front of the list.
     * 
     * @param propertyConfiguration
     *            The property configuration.
     * @return int Returns the number of configurations following the addition.
     */
    public int addConfigurationAtFront(PropertyConfiguration propertyConfiguration) {
        return addConfigurationAtIndex(propertyConfiguration, 0);
    }

    /**
     * Add a property configuration to a position.
     * 
     * @param propertyConfiguration
     *            The property configuration.
     * @param index
     *            The index position. (zero-indexed).
     * @return int Returns the number of configurations following the addition.
     */
    public int addConfigurationAtIndex(PropertyConfiguration propertyConfiguration, int index) {
        Preconditions.checkArgument(propertyConfiguration != null, "Property configuration must be initialized.");
        Preconditions.checkArgument(index >= 0, "Index position should be greater than or equal to 0.");

        AbstractConfiguration abstractConfiguration = propertyConfiguration.getAbstractConfiguration();
        String configurationName = propertyConfiguration.getConfigurationName();

        logger.debug("Attempting to add property configuration [Configuration name: {} | Index: {}]", configurationName, index);

        concurrentCompositeConfiguration.addConfigurationAtIndex(abstractConfiguration, configurationName, index);

        logger.info("Successfully added property configuration [Configuration name: {} | Index: {}]", configurationName, index);

        return numberOfConfigurations();
    }

    public int addConfigurationAtBack(PropertyConfiguration propertyConfiguration) {
        int numberOfConfigurations = numberOfConfigurations();

        return addConfigurationAtIndex(propertyConfiguration, numberOfConfigurations);
    }

    public Optional<AbstractConfiguration> removeConfiguration(String configurationName) {
        Preconditions.checkArgument(StringUtils.isNotBlank(configurationName), "Configuration name must not be blank.");

        // Workaround for Archaius bug - Need to remove by index since name will not get removed when removing a
        // configuration by the name. Filed: https://github.com/Netflix/archaius/issues/134
        AbstractConfiguration abstractConfiguration = null;

        Configuration configuration = concurrentCompositeConfiguration.getConfiguration(configurationName);
        List<AbstractConfiguration> configurationList = getConfigurations();
        for (int i = 0; i < configurationList.size(); i++) {
            if (configurationList.get(i).equals(configuration)) {
                abstractConfiguration = concurrentCompositeConfiguration.removeConfigurationAt(i);
                break;
            }
        }

        return Optional.fromNullable(abstractConfiguration);
    }

    public List<AbstractConfiguration> removeAllConfigurations() {
        List<AbstractConfiguration> abstractConfigurationList = Lists.newArrayList();

        Set<String> configurationNameSet = concurrentCompositeConfiguration.getConfigurationNames();
        for (String configurationName : configurationNameSet) {
            Optional<AbstractConfiguration> optionalAbstractConfiguration = removeConfiguration(configurationName);
            if (optionalAbstractConfiguration.isPresent()) {
                AbstractConfiguration abstractConfiguration = optionalAbstractConfiguration.get();
                abstractConfigurationList.add(abstractConfiguration);
            }
        }

        return abstractConfigurationList;
    }

    // Properties

    public boolean isPropertyPresent(String propertyName) {
        Preconditions.checkArgument(StringUtils.isNotBlank(propertyName), "Property name must not be blank.");

        return concurrentCompositeConfiguration.containsKey(propertyName);
    }

    private void initialize() {
        logger.info("Attempting to clear all existing property configurations [Number of configurations: {}]", numberOfConfigurations());

        concurrentCompositeConfiguration.clear();

        concurrentCompositeConfiguration.setThrowExceptionOnMissing(false);

        logger.info("Successfully cleared all property configurations [Number of configurations: {}]", numberOfConfigurations());
    }
}
