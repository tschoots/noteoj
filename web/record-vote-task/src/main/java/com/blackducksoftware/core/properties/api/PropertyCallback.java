package com.blackducksoftware.core.properties.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Property callback.
 *
 * This class is used to simplify the programming model surrounding property callbacks. Specifically, the hashCode and
 * equals methods are implemented to guarantee that adding a single PropertyCallback multiple times will not result in
 * multiple callbacks being executed.
 *
 * @author skatzman
 */
public abstract class PropertyCallback implements Runnable { // NOPMD DoNotUseThreads Archaius
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void run() {
        logger.debug("Executing property callback: {}", getClass());
        execute();
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public boolean equals(Object otherObject) {
        if (this == otherObject) {
            return true;
        }

        if ((null == otherObject) || (getClass() != otherObject.getClass())) {
            return false;
        }

        return true;
    }

    protected abstract void execute();
}
