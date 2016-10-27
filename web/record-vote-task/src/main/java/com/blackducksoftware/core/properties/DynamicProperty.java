package com.blackducksoftware.core.properties;

import com.blackducksoftware.core.properties.api.Property;
import com.google.common.base.MoreObjects;
import com.netflix.config.PropertyWrapper;

public class DynamicProperty<T> extends BaseDynamicProperty<T> implements Property<T> {
    private final T defaultValue;

    public DynamicProperty(PropertyWrapper<T> propertyWrapper) {
        this(propertyWrapper, null);
    }

    public DynamicProperty(PropertyWrapper<T> propertyWrapper, T defaultValue) {
        super(propertyWrapper);

        this.defaultValue = defaultValue;
    }

    /**
     * Gets the default value.
     *
     * @return T Returns the default value.
     */
    @Override
    public T getDefaultValue() {
        return defaultValue;
    }

    @Override
    public String toString() {
        String superString = super.toString();

        return MoreObjects.toStringHelper(this).omitNullValues()
                .addValue(superString)
                .add("Default value", defaultValue).toString();
    }
}
