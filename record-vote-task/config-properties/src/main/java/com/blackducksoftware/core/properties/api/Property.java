package com.blackducksoftware.core.properties.api;

/**
 * Property interface.
 * 
 * @author skatzman
 * 
 * @param <T>
 *            The property value type.
 */
public interface Property<T> {
    /**
     * Gets the property name.
     * 
     * @return String Returns the property name.
     */
    String getName();

    /**
     * Gets the property value.
     * 
     * @return T Returns the property value.
     */
    T getValue();

    /**
     * Gets the default value.
     * 
     * @return T Returns the default value.
     */
    T getDefaultValue();

    /**
     * Adds a callback. When the value of the property changes, the given property callback will be executed. The
     * equals()/hashCode() reference or exact object reference should be carefully managed when passing in a given
     * callback in order to prevent multiple callbacks that represent the same functionality from being added.
     * PropertyCallback gives a default implement that will cover this implementation in the vast majority of cases.
     * 
     * Note that if a property value is updated to the exact same value, the callback is not executed. For example, if
     * the value 'foo' is updated in the property source to the same value, 'foo', no callback will be executed for this
     * property value update.
     * 
     * @param propertyCallback
     *            The property callback.
     */
    void addCallback(PropertyCallback propertyCallback); // NOPMD DoNotUseThreads Archaius

    /**
     * Removed a callback.
     * 
     * @param propertyCallback
     *            The property callback.
     * @return boolean Returns true if the property callback was previously registered and false otherwise.
     */
    boolean removeCallback(PropertyCallback propertyCallback); // NOPMD DoNotUseThreads Archaius
}
