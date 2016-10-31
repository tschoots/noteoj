package com.blackducksoftware.core.properties;

import com.blackducksoftware.core.properties.api.PropertyCallback;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.netflix.config.PropertyWrapper;

/**
 * The base dynamic property.
 *
 * @author skatzman
 *
 * @param <T>
 *            The property value type.
 */
public abstract class BaseDynamicProperty<T> {
    /**
     * The property wrapper is the Archaius representation of the dynamic property. This is wrapped within this
     * implementation to not fully expose Archaius to our callers in order to control what can and cannot be done with
     * the dynamic property.
     *
     * Additional details could be exposed in the future:
     * - The timestamp of when this property was last changed (getChangedTimestamp).
     * - The ability to add validators for the given property (addValidator(PropertyChangeValidator)).
     */
    private final PropertyWrapper<T> propertyWrapper;

    public BaseDynamicProperty(PropertyWrapper<T> propertyWrapper) {
        Preconditions.checkArgument(propertyWrapper != null, "Property wrapper must be initialized.");

        this.propertyWrapper = propertyWrapper;
    }

    /**
     * Gets the property name.
     *
     * @return String Returns the property name.
     */
    public String getName() {
        return propertyWrapper.getName();
    }

    /**
     * Gets the property value.
     *
     * @return String Returns the property value.
     */
    public T getValue() {
        return propertyWrapper.getValue();
    }

    /**
     * Adds a callback to be executed upon property value change.
     *
     * @param propertyCallback
     *            The property callback.
     */
    public void addCallback(PropertyCallback propertyCallback) { // NOPMD DoNotUseThreads Archaius
        Preconditions.checkArgument(propertyCallback != null, "Property callback must be initialized.");

        propertyWrapper.addCallback(propertyCallback);
    }

    /**
     * Removes a callback.
     *
     * @param propertyCallback
     *            The property callback.
     * @return boolean Returns true if the callback was successfully removed and false otherwise.
     */
    public boolean removeCallback(PropertyCallback propertyCallback) { // NOPMD DoNotUseThreads Archaius
        Preconditions.checkArgument(propertyCallback != null, "Property callback must be initialized.");

        boolean isRemoved = false;

        com.netflix.config.DynamicProperty dynamicProperty = propertyWrapper.getDynamicProperty();
        if (dynamicProperty != null) {
            isRemoved = dynamicProperty.removeCallback(propertyCallback);
        }

        return isRemoved;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues()
                .add("Name", getName())
                .add("Value", getValue()).toString();
    }
}
