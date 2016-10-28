package com.blackducksoftware.core.properties.configuration.system;

import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.configuration.AbstractConfiguration;

import com.google.common.collect.Sets;

public class SystemPropertyConfiguration extends AbstractConfiguration {
    public SystemPropertyConfiguration() {
    }

    @Override
    public boolean isEmpty() {
        Properties properties = System.getProperties();
        return properties.isEmpty();
    }

    @Override
    public boolean containsKey(String key) {
        String value = System.getProperty(key);
        return (value != null);
    }

    @Override
    public Object getProperty(String key) {
        return System.getProperty(key);
    }

    @Override
    public Iterator<String> getKeys() {
        Properties properties = System.getProperties();
        Set<Object> keySet = properties.keySet();

        Set<String> keyStringSet = Sets.newHashSet();
        for (Object key : keySet) {
            if (key != null) {
                String keyString = key.toString();
                keyStringSet.add(keyString);
            }
        }

        return keyStringSet.iterator();
    }

    @Override
    protected void addPropertyDirect(String key, Object value) {
        // We do not support adding properties to this property configuration.
    }
}
