package com.blackducksoftware.core.message;

import java.util.Arrays;
import java.util.Objects;

/**
 * captures an error code together with names of arguments available to construct an internationalized message. These
 * argumentNames can be used in the actual translations in resource bundles.
 * To use the argument, put it in curly braces.
 *
 * @author ydeboeck
 *
 */
public class MessageDefinition {
    private final String code;

    private final String[] argumentNames;

    public MessageDefinition(String code, String... argumentNames) {
        this.code = code;
        this.argumentNames = argumentNames;
    }

    public String getCode() {
        return code;
    }

    public String[] getArgumentNames() {
        return argumentNames;
    }

    public int getNumberOfArgumentNames() {
        return (argumentNames != null) ? argumentNames.length : 0;
    }

    /**
     * puts curly braces around the code
     *
     * @param code
     * @return
     */
    public static final String curlied(String code) {
        return "{" + code + "}";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(argumentNames);
        result = prime * result + ((code == null) ? 0 : code.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        MessageDefinition other = (MessageDefinition) obj;
        if (!Arrays.equals(argumentNames, other.argumentNames)) {
            return false;
        }
        if (!Objects.equals(code, other.code)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "MessageCode [code=" + code + ", argumentNames=" + Arrays.toString(argumentNames) + "]";
    }
}
