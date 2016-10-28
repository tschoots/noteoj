package com.blackducksoftware.core.json;

import java.io.Serializable;
import java.util.Objects;

import javax.annotation.concurrent.Immutable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Convenience class that can be used to wrap Java objects that get serialized as Json primitives
 * into wrapper that gets serialized as a Json object
 * 
 * @since TWOM-2306
 * @see TWOM-2284
 * @see the Json RFC http://www.ietf.org/rfc/rfc4627.txt
 * 
 * @author bbrady
 * 
 * @param <T>
 *            the type of Java object to be wrapped as a Json Object: {@link Number} or {@link String}
 */
@Immutable
@JsonPropertyOrder({ "name", "value" })
public class JsonPrimitiveWrapper<T> implements Serializable {

    private final String name;

    private final T value;

    @JsonCreator
    public JsonPrimitiveWrapper(@JsonProperty("name") String name, @JsonProperty("value") T value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public T getValue() {
        return value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, value);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null) {
            return false;
        }

        if (getClass().equals(obj.getClass()))
        {
            final JsonPrimitiveWrapper<T> otherWrapper = (JsonPrimitiveWrapper<T>) obj;
            return Objects.equals(name, otherWrapper.getName()) && Objects.equals(value, otherWrapper.getValue());
        }
        return false;
    }
}
