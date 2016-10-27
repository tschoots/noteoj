package com.blackducksoftware.core.properties;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.ConversionService;

//import com.blackducksoftware.core.crypto.CryptoUtil;
import com.blackducksoftware.core.properties.api.Property;
import com.blackducksoftware.core.properties.api.PropertyManager;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.netflix.config.ConcurrentCompositeConfiguration;
import com.netflix.config.DynamicBooleanProperty;
import com.netflix.config.DynamicContextualProperty;
import com.netflix.config.DynamicDoubleProperty;
import com.netflix.config.DynamicFloatProperty;
import com.netflix.config.DynamicIntProperty;
import com.netflix.config.DynamicLongProperty;
import com.netflix.config.DynamicPropertyFactory;
import com.netflix.config.DynamicStringProperty;

/**
 * Dynamic property manager. Would this work better as a standalone singleton?
 *
 * @author skatzman
 *
 */
public final class DynamicPropertyManager implements PropertyManager {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final DynamicPropertyFactory dynamicPropertyFactory;

    private final ConversionService conversionService;

    private final PropertyExceptionHandler propertyExceptionHandler;

    public DynamicPropertyManager(ConversionService conversionService, PropertyExceptionHandler propertyExceptionHandler) {
        Preconditions.checkArgument(conversionService != null, "Conversion service must be initialized.");
        Preconditions.checkArgument(propertyExceptionHandler != null, "Property exception handler must be initialized.");

        dynamicPropertyFactory = DynamicPropertyFactory.getInstance();

        this.conversionService = conversionService;
        this.propertyExceptionHandler = propertyExceptionHandler;
    }

    protected void debugDynamicPropertyManager() {
        Object backingConfigurationSource = DynamicPropertyFactory.getBackingConfigurationSource();
        if (backingConfigurationSource instanceof ConcurrentCompositeConfiguration) {
            ConcurrentCompositeConfiguration concurrentCompositeConfiguration = (ConcurrentCompositeConfiguration) backingConfigurationSource;
            Properties properties = concurrentCompositeConfiguration.getProperties();
            for (Entry<Object, Object> entry : properties.entrySet()) {
                logger.info("*** Retrieved property [Key: {} | Value: {}].", entry.getKey(), entry.getValue());
            }

            logger.info("*** Is initialized with default config?: {}", DynamicPropertyFactory.isInitializedWithDefaultConfig());
            logger.info("*** Is throw missing configuration source exception?: {}", DynamicPropertyFactory.isThrowMissingConfigurationSourceException());
        }
    }

    @Override
    public boolean isPropertyPresent(String propertyName) {
        Optional<String> optionalResultValue = retrieveRequiredPropertyValue(propertyName);

        return optionalResultValue.isPresent();
    }

    @Override
    public String getRequiredPropertyValue(String propertyName) {
        Optional<String> optionalResultValue = retrieveRequiredPropertyValue(propertyName);
        if (!optionalResultValue.isPresent()) {
            // Property does not exist.
            throw propertyExceptionHandler.createPropertyException(propertyName, PropertyMessages.REQUIRED_PROPERTY_DOES_NOT_EXIST, propertyName);
        }

        return optionalResultValue.get();
    }

    @Override
    public <T> T getRequiredPropertyValue(String propertyName, Class<T> propertyClass) {
        Preconditions.checkArgument(StringUtils.isNotBlank(propertyName), "Property name must not be blank.");
        Preconditions.checkArgument(propertyClass != null, "Property name must not be blank.");

        // Ideally, we would just use contextual retrieval, but Archaius only supports default values and if a null
        // default value is provided for the contextual call, an NPE occurs.

        T resultValue = null;

        DynamicStringProperty dynamicStringProperty = dynamicPropertyFactory.getStringProperty(propertyName, null);
        String resultValueString = dynamicStringProperty.get();
        if (resultValueString != null) {
            try {
                resultValue = conversionService.convert(resultValueString, propertyClass);
            } catch (ConversionFailedException e) {
                // Property can not be converted.
                throw propertyExceptionHandler.createPropertyException(e, propertyName, PropertyMessages.REQUIRED_PROPERTY_DOES_NOT_CONVERT, propertyName,
                        propertyClass);
            }
        } else {
            // Property does not exist.
            throw propertyExceptionHandler.createPropertyException(propertyName, PropertyMessages.REQUIRED_PROPERTY_DOES_NOT_EXIST, propertyName);
        }

        return resultValue;
    }

    @Override
    public Property<Boolean> getBooleanProperty(String propertyName, boolean defaultValue) {
        Preconditions.checkArgument(StringUtils.isNotBlank(propertyName), "Property name must not be blank.");

        DynamicBooleanProperty dynamicBooleanProperty = dynamicPropertyFactory.getBooleanProperty(propertyName, defaultValue);

        return new DynamicProperty<Boolean>(dynamicBooleanProperty, defaultValue);
    }

    @Override
    public Boolean getBooleanPropertyValue(String propertyName, boolean defaultValue) {
        return getBooleanProperty(propertyName, defaultValue).getValue();
    }

    @Override
    public Property<Double> getDoubleProperty(String propertyName, double defaultValue) {
        Preconditions.checkArgument(StringUtils.isNotBlank(propertyName), "Property name must not be blank.");

        DynamicDoubleProperty dynamicDoubleProperty = dynamicPropertyFactory.getDoubleProperty(propertyName, defaultValue);

        return new DynamicProperty<Double>(dynamicDoubleProperty, defaultValue);
    }

    @Override
    public Double getDoublePropertyValue(String propertyName, double defaultValue) {
        return getDoubleProperty(propertyName, defaultValue).getValue();
    }

    @Override
    public Property<Float> getFloatProperty(String propertyName, float defaultValue) {
        Preconditions.checkArgument(StringUtils.isNotBlank(propertyName), "Property name must not be blank.");

        DynamicFloatProperty dynamicFloatProperty = dynamicPropertyFactory.getFloatProperty(propertyName, defaultValue);

        return new DynamicProperty<Float>(dynamicFloatProperty, defaultValue);
    }

    @Override
    public Float getFloatPropertyValue(String propertyName, float defaultValue) {
        return getFloatProperty(propertyName, defaultValue).getValue();
    }

    @Override
    public Property<Integer> getIntegerProperty(String propertyName, int defaultValue) {
        Preconditions.checkArgument(StringUtils.isNotBlank(propertyName), "Property name must not be blank.");

        DynamicIntProperty dynamicIntProperty = dynamicPropertyFactory.getIntProperty(propertyName, defaultValue);

        return new DynamicProperty<Integer>(dynamicIntProperty, defaultValue);
    }

    @Override
    public Integer getIntegerPropertyValue(String propertyName, int defaultValue) {
        return getIntegerProperty(propertyName, defaultValue).getValue();
    }

    @Override
    public Property<Long> getLongProperty(String propertyName, long defaultValue) {
        Preconditions.checkArgument(StringUtils.isNotBlank(propertyName), "Property name must not be blank.");

        DynamicLongProperty dynamicLongProperty = dynamicPropertyFactory.getLongProperty(propertyName, defaultValue);

        return new DynamicProperty<Long>(dynamicLongProperty, defaultValue);
    }

    @Override
    public Long getLongPropertyValue(String propertyName, long defaultValue) {
        return getLongProperty(propertyName, defaultValue).getValue();
    }

    @Override
    public Property<String> getProperty(String propertyName, String defaultValue) {
        Preconditions.checkArgument(StringUtils.isNotBlank(propertyName), "Property name must not be blank.");

        DynamicStringProperty dynamicStringProperty = dynamicPropertyFactory.getStringProperty(propertyName, defaultValue);

        return new DynamicProperty<String>(dynamicStringProperty, defaultValue);
    }

    @Override
    public String getEncodedPropertyValue(String propertyName, String defaultValue) {
        String propertyValue = defaultValue;

        Property<String> returnProperty = getProperty(propertyName, defaultValue);
        if (StringUtils.isNotBlank(returnProperty.getValue())) {
            String encodedValue = returnProperty.getValue();
            // Using the key below to be linked with the encoding AppMgr does via the "Misc" class.
            // HUB-971 created to address this long term
            try {
               // propertyValue = CryptoUtil.decodeDes("Irt6#Nq_@", CryptoUtil.DES, encodedValue);
                logger.error("you will not get here");
            } catch (RuntimeException e) {
                logger.warn("Unable to decrypt value for property, '{}': {}", propertyName, e.getMessage());
                logger.debug("Unable to decrypt value for property, '{}'.", propertyName, e);
            }
        }

        return propertyValue;
    }

    @Override
    public String getPropertyValue(String propertyName, String defaultValue) {
        return getProperty(propertyName, defaultValue).getValue();
    }

    @Override
    public <T> Property<T> getGenericProperty(String propertyName, T defaultValue) {
        Preconditions.checkArgument(StringUtils.isNotBlank(propertyName), "Property name must not be blank.");
        Preconditions.checkArgument(defaultValue != null, "Default value must be initialized.");

        DynamicContextualProperty<T> dynamicContextualProperty = dynamicPropertyFactory.getContextualProperty(propertyName, defaultValue);

        return new DynamicProperty<T>(dynamicContextualProperty, defaultValue);
    }

    @Override
    public <T> Property<T> getJsonProperty(String propertyName, T defaultValue, Class<T> targetClass) {
        Preconditions.checkArgument(StringUtils.isNotBlank(propertyName), "Property name must not be blank.");
        Preconditions.checkArgument(targetClass != null, "Target class must be initialized.");

        DynamicStringProperty dynamicStringProperty = dynamicPropertyFactory.getStringProperty(propertyName, null);

        return new DynamicJsonProperty<T>(dynamicStringProperty, defaultValue, targetClass);
    }

    @Override
    public Map<String, String> getPropertyMap() {
        Object backingConfigurationSource = DynamicPropertyFactory.getBackingConfigurationSource();
        if (backingConfigurationSource instanceof ConcurrentCompositeConfiguration) {
            Map<String, String> propertyMap = Maps.newTreeMap();

            ConcurrentCompositeConfiguration concurrentCompositeConfiguration = (ConcurrentCompositeConfiguration) backingConfigurationSource;
            Properties properties = concurrentCompositeConfiguration.getProperties();
            for (Entry<Object, Object> entry : properties.entrySet()) {
                Object keyObject = entry.getKey();
                Object valueObject = entry.getValue();
                if ((keyObject != null) && (valueObject != null)) {
                    String key = keyObject.toString();
                    String value = valueObject.toString();
                    propertyMap.put(key, value);
                }
            }

            return propertyMap;
        }

        return Maps.newTreeMap();
    }

    private Optional<String> retrieveRequiredPropertyValue(String propertyName) {
        Preconditions.checkArgument(StringUtils.isNotBlank(propertyName), "Property name must not be blank.");

        DynamicStringProperty dynamicStringProperty = dynamicPropertyFactory.getStringProperty(propertyName, null);
        String resultValue = dynamicStringProperty.get();

        return Optional.fromNullable(resultValue);
    }
}
