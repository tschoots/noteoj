package com.blackducksoftware.core.properties.configuration.writeable;

import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.configuration.AbstractConfiguration;

import com.google.common.collect.Sets;

public class DatabasePropertyConfiguration extends AbstractConfiguration {
	
	private final DatabasePolledConfigurationSource configSource;
	
    public DatabasePropertyConfiguration(DatabasePolledConfigurationSource source) {
    	this.configSource = source;
    }

    @Override
    public boolean isEmpty() {
        Map<String, Object> propMap = configSource.getDatabasePropertyMap();
        return propMap.isEmpty();
    }

    @Override
    public boolean containsKey(String key) {
        Map<String, Object> propMap = configSource.getDatabasePropertyMap();
        return propMap.containsKey(key);
    }

    @Override
    public Object getProperty(String key) {
        Map<String, Object> propMap = configSource.getDatabasePropertyMap();
        return propMap.get(key);
    }

    @Override
    public Iterator<String> getKeys() {
        Map<String, Object> propMap = configSource.getDatabasePropertyMap();
        Set<String> keySet = propMap.keySet();

        Set<String> keyStringSet = Sets.newHashSet();
        for (String key : keySet) {
            if (key != null) {
                keyStringSet.add(key);
            }
        }

        return keyStringSet.iterator();
    }

    @Override
    protected void addPropertyDirect(String key, Object value) {
        // We do not support adding properties to this property configuration.
    }
}
