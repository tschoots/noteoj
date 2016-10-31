package com.blackducksoftware.core.properties.configuration.system;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.netflix.config.PollResult;
import com.netflix.config.PolledConfigurationSource;

/**
 * The system property polled configuration source. Periodically called by the property infrastructure to return a poll
 * result containing the full map of system property key/value pairs.
 * 
 * @author skatzman
 * 
 */
public class SystemPropertyPolledConfigurationSource implements PolledConfigurationSource {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * Default constructor.
     */
    public SystemPropertyPolledConfigurationSource() {
    }

    @Override
    public PollResult poll(boolean initial, Object checkPoint) throws Exception { // NOPMD
                                                                                  // SignatureDeclareThrowsException
        // 3rd-party library abstract method, must throw Exception.

        // Return a full result of all current, supported system property values.
        logger.debug("Attempting to poll for system property values.");

        Map<String, Object> systemPropertyMap = Maps.newHashMap();

        Properties properties = System.getProperties();
        Set<Entry<Object, Object>> entrySet = properties.entrySet();
        for (Entry<Object, Object> entry : entrySet) {
            Object keyObject = entry.getKey();

            if ((keyObject != null) && (keyObject instanceof String)) {
                String key = (String) keyObject;
                Object value = entry.getValue();
                systemPropertyMap.put(key, value);
            }
        }

        if (logger.isDebugEnabled()) {
            final String threadName = Thread.currentThread().getName();
            logger.debug("Successfully polled for system property values [Number of properties: {} | Thread name: {}].", systemPropertyMap.size(), threadName);
        }

        return PollResult.createFull(systemPropertyMap);
    }
}
