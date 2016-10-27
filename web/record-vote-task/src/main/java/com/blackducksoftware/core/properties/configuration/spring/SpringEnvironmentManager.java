package com.blackducksoftware.core.properties.configuration.spring;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;

/**
 * In order to accommodate changes to the Spring environment due to Spring application context reloads (usually in a
 * testing environment), the environment property sources are managed here for the PropertyManager.
 * 
 * This is done rather than simply re-initializing the PropertyManager because Archaius has a static interface towards
 * installing the property configuration. In the same JVM, re-installing the configuration causes an error in Archaius
 * (by their design).
 * 
 * This mechanism works around that by manipulating the Spring environment based on the constantly changing property
 * sources that occur during configuration loading during our tests.
 * 
 * @author skatzman
 * 
 */
public class SpringEnvironmentManager {
    private static volatile AbstractEnvironment environment;

    private static volatile Map<String, MapPropertySource> mapPropertySourceMap = Maps.newHashMap();

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public SpringEnvironmentManager() {
    }

    public Environment getEnvironment() {
        synchronized (SpringEnvironmentManager.class) {
            return environment;
        }
    }

    public List<MapPropertySource> getMapPropertySourceList() {
        synchronized (SpringEnvironmentManager.class) {
            Collection<MapPropertySource> mapPropertySourceCollection = mapPropertySourceMap.values();
            return ImmutableList.copyOf(mapPropertySourceCollection);
        }
    }

    public boolean containsProperty(String propertyName) {
        synchronized (SpringEnvironmentManager.class) {
            return environment.containsProperty(propertyName);
        }
    }

    public String getProperty(String propertyName) {
        synchronized (SpringEnvironmentManager.class) {
            return environment.getProperty(propertyName);
        }
    }

    public String getProperty(String propertyName, String defaultValue) {
        synchronized (SpringEnvironmentManager.class) {
            return environment.getProperty(propertyName, defaultValue);
        }
    }

    public void initialize(Environment updatedEnvironment) {
        synchronized (SpringEnvironmentManager.class) {
            Preconditions.checkArgument(updatedEnvironment != null, "Environment must be initialized.");

            logger.info("Initializing Spring property configuration environment: {}", updatedEnvironment.getClass());

            Preconditions.checkArgument(updatedEnvironment instanceof AbstractEnvironment, "Environment must be an abstract environment.");

            // Remove old property sources
            AbstractEnvironment updatedAbstractEnvironment = (AbstractEnvironment) updatedEnvironment;
            Map<String, MapPropertySource> updatedMapPropertySourceMap = retrieveMapPropertySourceMap(updatedAbstractEnvironment);

            // Set<String> updatedMapPropertySourceNameSet = updatedMapPropertySourceMap.keySet();
            // Set<String> propertySourceNamesToRemove =
            // determinePropertySourceNamesToRemove(updatedMapPropertySourceNameSet);
            // removePropertySources(propertySourceNamesToRemove);

            addOrReplacePropertySources(updatedAbstractEnvironment, updatedMapPropertySourceMap);

            // environment = updatedAbstractEnvironment;
            // mapPropertySourceMap = updatedMapPropertySourceMap;
        }
    }

    private Map<String, MapPropertySource> retrieveMapPropertySourceMap(AbstractEnvironment abstractEnvironment) {
        Map<String, MapPropertySource> resultMap = Maps.newHashMap();

        MutablePropertySources mutablePropertySources = abstractEnvironment.getPropertySources();
        if (mutablePropertySources != null) {
            Iterator<PropertySource<?>> propertySourceIterator = mutablePropertySources.iterator();
            while (propertySourceIterator.hasNext()) {
                PropertySource<?> propertySource = propertySourceIterator.next();
                if (propertySource instanceof MapPropertySource) {
                    MapPropertySource mapPropertySource = (MapPropertySource) propertySource;
                    String name = mapPropertySource.getName();

                    resultMap.put(name, mapPropertySource);
                }
            }
        }

        return resultMap;
    }

    private void addOrReplacePropertySources(AbstractEnvironment abstractEnvironment, Map<String, MapPropertySource> updatedMapPropertySourceMap) {
        if (environment != null) {
            MutablePropertySources mutablePropertySources = environment.getPropertySources();
            Set<Entry<String, MapPropertySource>> entrySet = updatedMapPropertySourceMap.entrySet();
            for (Entry<String, MapPropertySource> entry : entrySet) {
                String propertySourceName = entry.getKey();
                MapPropertySource mapPropertySource = entry.getValue();

                // Add or replace to the property source map
                mapPropertySourceMap.put(propertySourceName, mapPropertySource);

                if (mapPropertySourceMap.containsKey(propertySourceName)) {
                    logger.info("Replacing existing property source: {}", propertySourceName);

                    // Remove from the environment
                    mutablePropertySources.remove(propertySourceName);
                } else {
                    logger.info("Adding new property source: {}", propertySourceName);
                }

                // Add new or back to the environment
                mutablePropertySources.addFirst(mapPropertySource);
            }
        } else {
            logger.info("Assigning new environment.");

            environment = abstractEnvironment;
            MutablePropertySources mutablePropertySources = environment.getPropertySources();
            Iterator<PropertySource<?>> propertySourceIterator = mutablePropertySources.iterator();
            while (propertySourceIterator.hasNext()) {
                PropertySource<?> propertySource = propertySourceIterator.next();
                if (propertySource instanceof MapPropertySource) {
                    MapPropertySource mapPropertySource = (MapPropertySource) propertySource;
                    String propertySourceName = mapPropertySource.getName();

                    logger.info("Adding new property source: {}", propertySourceName);

                    // Add to the property map
                    mapPropertySourceMap.put(propertySourceName, mapPropertySource);
                }
            }
        }
    }
}
