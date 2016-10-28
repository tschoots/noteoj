package com.blackducksoftware.core.config;

import java.io.IOException;

import org.slf4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.support.ResourcePropertySource;
import org.springframework.util.Assert;

/**
 * Enable command line application configuration
 * <p>
 * Passing "-Dblackduck.propertyfile=file:./custom.properties" will override application properties. See
 * {@link rg.springframework.core.io.support.ResourcePropertySource} for options.
 * 
 * Exclusion: Spring environment should be used for this class, not the PropertyManager.
 * 
 * @author gnerboe
 * @since TWOM-348
 */
public class PropertiesPostProcessor implements BeanFactoryPostProcessor {

    public static final String BDS_PROPERTY_FILE_KEY = "blackduck.propertyfile";

    private final Logger logger = ConfigUtil.getLogger();

    private ResourcePropertySource getPropertySource() {
        ResourcePropertySource ps = null;
        String resource = null;
        try {
            resource = System.getProperty(BDS_PROPERTY_FILE_KEY);
            if (resource != null) {
                logger.info("Read properties from Resource '{}'....", resource);
                ps = new ResourcePropertySource(BDS_PROPERTY_FILE_KEY, resource);
            } else {
                logger.info("No property '{}' defined", BDS_PROPERTY_FILE_KEY);
            }
        } catch (IOException ioe) {
            Assert.notNull(null, "Failed to read overridden properties from '" + resource + "': " + ioe);
        }
        return ps;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

        logger.debug("PropertiesPostProcessor.postProcessBeanFactory(ConfigurableListableBeanFactory)... ");
        ConfigurableEnvironment env = getEnvironment(beanFactory);
        env.setPlaceholderPrefix("@{");

        ResourcePropertySource propertySource = getPropertySource();
        if (propertySource != null) {
            MutablePropertySources mps = env.getPropertySources();
            logger.debug("PropertiesPostProcessor() - addFirst properties.");
            mps.addAfter(StandardEnvironment.SYSTEM_PROPERTIES_PROPERTY_SOURCE_NAME, propertySource);
        } else {
            logger.debug("PropertiesPostProcessor() - PropertySource==null, no properties added.");
        }
    }

    private ConfigurableEnvironment getEnvironment(ConfigurableListableBeanFactory beanFactory) {
        Object envObj = beanFactory.getBean(ConfigurableApplicationContext.ENVIRONMENT_BEAN_NAME);
        Assert.notNull(envObj, "Environment must be present (ConfigurableApplicationContext.ENVIRONMENT_BEAN_NAME)");
        Assert.isInstanceOf(ConfigurableEnvironment.class, envObj,
                "Invalid state. Environment is not an instance of ConfigurableEnvironment (has flush() been invoked?).");
        ConfigurableEnvironment env = (ConfigurableEnvironment) envObj;
        return env;
    }

}
