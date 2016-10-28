package com.blackducksoftware.core.properties.api;

import java.util.Map;

/**
 * Property manager interface.
 * 
 * @author skatzman
 */
public interface PropertyManager {
    /**
     * Determines if a given property is present.
     * 
     * @param propertyName
     *            The property name. Must not be blank.
     * @return boolean Returns true if the property exists and false otherwise.
     */
    boolean isPropertyPresent(String propertyName);

    /**
     * Gets the literal string required property value. A PropertyException is thrown if the property does not exist.
     * 
     * @param propertyName
     *            The property name. Must not be blank.
     * @return String Returns the string required property value.
     */
    String getRequiredPropertyValue(String propertyName);

    /**
     * Gets the literal required property value. A PropertyException is thrown if the property does not exist.
     * 
     * @param propertyName
     *            The property name. Must not be blank.
     * @param propertyClass
     *            The property class. Must be initialized.
     * @return T Returns the property class.
     */
    <T> T getRequiredPropertyValue(String propertyName, Class<T> propertyClass);

    /**
     * Gets a dynamic Boolean property.
     * 
     * @param propertyName
     *            The property name. Must not be blank.
     * @param defaultValue
     *            The default value.
     * @return Property<Boolean> Returns the dynamic Boolean property.
     */
    Property<Boolean> getBooleanProperty(String propertyName, boolean defaultValue);

    /**
     * Gets the current Boolean property value. Used for backwards-compatibility.
     * 
     * @param propertyName
     *            The property name. Must not be blank.
     * @param defaultValue
     *            The default value.
     * @return Boolean Returns the current Boolean property value.
     */
    Boolean getBooleanPropertyValue(String propertyName, boolean defaultValue);

    /**
     * Gets a dynamic Double property.
     * 
     * @param propertyName
     *            The property name. Must not be blank.
     * @param defaultValue
     *            The default value.
     * @return Property<Double> Returns the dynamic Double property.
     */
    Property<Double> getDoubleProperty(String propertyName, double defaultValue);

    /**
     * Gets the current Double property value. Used for backwards-compatibility.
     * 
     * @param propertyName
     *            The property name. Must not be blank.
     * @param defaultValue
     *            The default value.
     * @return Double Returns the current Double property value.
     */
    Double getDoublePropertyValue(String propertyName, double defaultValue);

    /**
     * Gets a dynamic Float property.
     * 
     * @param propertyName
     *            The property name. Must not be blank.
     * @param defaultValue
     *            The default value.
     * @return Property<Float> Returns the dynamic Float property.
     */
    Property<Float> getFloatProperty(String propertyName, float defaultValue);

    /**
     * Gets the current Float property value. Used for backwards-compatibility.
     * 
     * @param propertyName
     *            The property name. Must not be blank.
     * @param defaultValue
     *            The default value.
     * @return Float Returns the current Float property value.
     */
    Float getFloatPropertyValue(String propertyName, float defaultValue);

    /**
     * Gets a dynamic Integer property.
     * 
     * @param propertyName
     *            The property name. Must not be blank.
     * @param defaultValue
     *            The default value.
     * @return Property<Integer> Returns the dynamic Integer property.
     */
    Property<Integer> getIntegerProperty(String propertyName, int defaultValue);

    /**
     * Gets the current Integer property value. Used for backwards-compatibility.
     * 
     * @param propertyName
     *            The property name. Must not be blank.
     * @param defaultValue
     *            The default value.
     * @return Integer Returns the current Integer property value.
     */
    Integer getIntegerPropertyValue(String propertyName, int defaultValue);

    /**
     * Gets a dynamic Long property.
     * 
     * @param propertyName
     *            The property name. Must not be blank.
     * @param defaultValue
     *            The default value.
     * @return Property<Long> Returns the dynamic Long property.
     */
    Property<Long> getLongProperty(String propertyName, long defaultValue);

    /**
     * Gets the current Long property value. Used for backwards-compatibility.
     * 
     * @param propertyName
     *            The property name. Must not be blank.
     * @param defaultValue
     *            The default value.
     * @return Long Returns the current Long property value.
     */
    Long getLongPropertyValue(String propertyName, long defaultValue);

    /**
     * Gets a dynamic String property.
     * 
     * @param propertyName
     *            The property name. Must not be blank.
     * @param defaultValue
     *            The default value.
     * @return Property<String> Returns the dynamic String property.
     */
    Property<String> getProperty(String propertyName, String defaultValue);

    /**
     * Gets the current String property value. Used for backwards-compatibility.
     * 
     * @param propertyName
     *            The property name. Must not be blank.
     * @param defaultValue
     *            The default value.
     * @return String Returns the current String property value.
     */
    String getPropertyValue(String propertyName, String defaultValue);

    /**
     * Gets the encoded stored String property value and decodes it.
     * 
     * @param propertyName
     *            The property name. Must not be blank.
     * @param defaultValue
     *            The default value.
     * @return String Returns the decoded String property.
     */
    String getEncodedPropertyValue(String propertyName, String defaultValue);

    /**
     * Gets a generic property. The generic default value must be initialized.
     * 
     * @param propertyName
     *            The property name. Must not be blank.
     * @param defaultValue
     *            The default value. Must be initialized.
     * @return Property<T> Returns the generic property.
     */
    <T> Property<T> getGenericProperty(String propertyName, T defaultValue);

    /**
     * Gets a JSON property deserializing it to a Java object.
     * 
     * @param propertyName
     *            The property name. Must not be blank.
     * @param defaultValue
     *            The default value.
     * @param targetClass
     *            The target class. Must be initialized.
     * @return Property<T> Returns the JSON property.
     */
    <T> Property<T> getJsonProperty(String propertyName, T defaultValue, Class<T> targetClass);

    /**
     * Gets the property map. The map key represents the property name. The map value represents the property value in
     * String format.
     * 
     * @return Map<String, String> Returns the property map.
     */
    Map<String, String> getPropertyMap();
}
