package com.blackducksoftware.core.properties.configuration.spring;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.configuration.AbstractConfiguration;
import org.springframework.core.env.MapPropertySource;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;

public class SpringPropertyConfiguration extends AbstractConfiguration {
    private final SpringEnvironmentManager springEnvironmentManager;

    public SpringPropertyConfiguration(SpringEnvironmentManager springEnvironmentManager) {
        Preconditions.checkArgument(springEnvironmentManager != null, "Spring environment manager must be initialized.");

        this.springEnvironmentManager = springEnvironmentManager;
    }

    @Override
    public boolean isEmpty() {
        List<MapPropertySource> mapPropertySourceList = springEnvironmentManager.getMapPropertySourceList();

        for (MapPropertySource mapPropertySource : mapPropertySourceList) {
            Map<String, Object> sourceMap = mapPropertySource.getSource();
            if ((sourceMap != null) && (!sourceMap.isEmpty())) {
                // At least one property source is initialized and has elements.
                // This property configuration is not empty.
                return false;
            }
        }

        // No property sources are initialized and/or all property sources are empty.
        return true;
    }

    @Override
    public boolean containsKey(String key) {
        return springEnvironmentManager.containsProperty(key);
    }

    @Override
    public Object getProperty(String key) {
        return springEnvironmentManager.getProperty(key);
    }

    @Override
    public Iterator<String> getKeys() {
        Set<String> keySet = Sets.newHashSet();

        List<MapPropertySource> mapPropertySourceList = springEnvironmentManager.getMapPropertySourceList();

        for (MapPropertySource mapPropertySource : mapPropertySourceList) {
            String[] propertyNames = mapPropertySource.getPropertyNames();
            if (propertyNames != null) {
                List<String> propertyNameList = Arrays.asList(propertyNames);
                keySet.addAll(propertyNameList);
            }
        }

        return keySet.iterator();
    }

    @Override
    protected void addPropertyDirect(String key, Object value) {
        // We do not support adding properties to this property configuration.
    }
}
