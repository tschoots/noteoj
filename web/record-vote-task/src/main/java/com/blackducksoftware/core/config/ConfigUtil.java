package com.blackducksoftware.core.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ConfigUtil {

    private static final Logger logger = LoggerFactory.getLogger("com.blackducksoftware.springconfig");

    private ConfigUtil() {
    }

    public static Logger getLogger() {
        return logger;
    }

    public static void logConfig(Object config) {
        logger.info("Loading {}", config.getClass());
    }

}
