package com.blackducksoftware.core.properties.configuration.writeable;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.netflix.config.WatchedConfigurationSource;
import com.netflix.config.WatchedUpdateListener;
import com.netflix.config.WatchedUpdateResult;

/**
 * A watched configuration source. The goal of this class is to provide the ability to add a watched configuration
 * source that is writeable and can track changes for a single property value.
 * 
 * @author skatzman
 * 
 */
public class SingleValueConfigurationSource implements WatchedConfigurationSource {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final List<WatchedUpdateListener> watchedUpdateListenerList = Lists.newCopyOnWriteArrayList();

    private final String propertyName;

    /**
     * Single watched value.
     */
    private volatile AtomicReference<String> value = new AtomicReference<>();

    /**
     * Main constructor.
     * 
     * @param propertyName
     *            The property name.
     */
    public SingleValueConfigurationSource(String propertyName) {
        Preconditions.checkArgument(StringUtils.isNotBlank(propertyName), "Property name must not be blank.");

        this.propertyName = propertyName;
    }

    @Override
    public void addUpdateListener(WatchedUpdateListener watchUpdateListener) {
        if (watchUpdateListener != null) {
            watchedUpdateListenerList.add(watchUpdateListener);
        }
    }

    @Override
    public void removeUpdateListener(WatchedUpdateListener watchUpdateListener) {
        if (watchUpdateListener != null) {
            watchedUpdateListenerList.remove(watchUpdateListener);
        }
    }

    @Override
    public Map<String, Object> getCurrentData() throws Exception { // NOPMD: SignatureDeclareThrowsException
        // Throws Exception is required due to implementing 3rd-party Archaius library
        logger.debug("Attempting to retrieve current data.");

        String propertyValue = value.get();
        Map<String, Object> currentDataMap = createCompleteDataMap(propertyValue);

        logger.debug("Successfully retrieved current data: {}", propertyValue);

        return currentDataMap;
    }

    public String getPropertyName() {
        return propertyName;
    }

    /**
     * Gets the value.
     * 
     * @return String Returns the value. Can be null.
     */
    public String getValue() {
        return value.get();
    }

    /**
     * Sets the value.
     * 
     * @param value
     *            The value.
     * @return String Returns the previously set singular value. Can be null.
     * @throws Exception
     */
    public String setValue(String value) {
        String oldValue = this.value.getAndSet(value);

        fireEvent(value);

        return oldValue;
    }

    public int getNumberOfWatchedUpdateListeners() {
        return watchedUpdateListenerList.size();
    }

    protected void fireEvent(String value) {
        // Use new map rather than getCurrentData given this value to avoid synchronization issues.
        Map<String, Object> completeDataMap = createCompleteDataMap(value);
        WatchedUpdateResult watchedUpdateResult = WatchedUpdateResult.createFull(completeDataMap);

        for (WatchedUpdateListener watchedUpdateListener : watchedUpdateListenerList) {
            final String watchedUpdateListenerClassName = watchedUpdateListener.getClass().getName();
            logger.debug("Attempting to fire event for watched update listener [Property name: {} | Value: {} | Class: {}]", propertyName, value,
                    watchedUpdateListenerClassName);

            try {
                watchedUpdateListener.updateConfiguration(watchedUpdateResult);

                logger.debug("Successfully fired event for watched update listener [Property name: {} | Value: {} | Class: {}]", propertyName, value,
                        watchedUpdateListenerClassName);
            } catch (Exception e) {
                // Archaius design pattern catches Throwable which is an anti-pattern.
                // To lessen the severity and let Errors go up the stack, catching Exception is the lesser of two evils.
                logger.error("Unable to fire event.", e);
            }
        }
    }

    private Map<String, Object> createCompleteDataMap(String propertyValue) {
        Map<String, Object> completeDataMap = Maps.newHashMap();

        if (propertyValue != null) {
            completeDataMap.put(propertyName, propertyValue);
        }

        return completeDataMap;
    }
}
