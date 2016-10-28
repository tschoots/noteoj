package com.blackducksoftware.core.properties;

import javax.validation.MessageInterpolator;
import java.sql.*;

import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.hibernate.validator.spi.resourceloading.ResourceBundleLocator;

import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.validation.beanvalidation.LocaleContextMessageInterpolator;
import org.springframework.validation.beanvalidation.MessageSourceResourceBundleLocator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.env.Environment;

import com.blackducksoftware.core.config.PropertiesPostProcessor;
import com.blackducksoftware.core.message.impl.MessageLookup;
import com.blackducksoftware.core.message.spring.CoreResourceBundleExtension;
import com.blackducksoftware.core.message.spring.CoreResourceBundleMessageSource;
import com.blackducksoftware.core.exception.BlackDuckServerException;

import com.blackducksoftware.core.properties.api.PropertyConstants;
import com.blackducksoftware.core.properties.api.PropertyManager;
import com.blackducksoftware.core.properties.api.Property;
import com.blackducksoftware.core.properties.configuration.DynamicPropertyConfigurationManager;
import com.blackducksoftware.core.properties.configuration.PropertyConfiguration;
import com.blackducksoftware.core.properties.configuration.PropertyConfigurationName;
import com.blackducksoftware.core.properties.configuration.spring.SpringEnvironmentManager;
import com.blackducksoftware.core.properties.configuration.spring.SpringPropertyConfiguration;
import com.blackducksoftware.core.properties.configuration.spring.SpringPropertyConfigurationFactory;
import com.blackducksoftware.core.properties.configuration.system.SystemPropertyConfigurationFactory;
import com.blackducksoftware.core.properties.configuration.writeable.DatabasePropertyConfiguration;
import com.blackducksoftware.core.properties.configuration.writeable.DatabaseConfigurationFactory;
import com.blackducksoftware.core.properties.configuration.writeable.DatabasePolledConfigurationSource;
import com.blackducksoftware.core.properties.configuration.writeable.SingleValueConfigurationSource;
import com.blackducksoftware.core.properties.configuration.writeable.SingleValuePropertyConfigurationFactory;
import com.google.common.base.Optional;
import com.google.common.base.Charsets;


public class PropertyServer {

  private DynamicPropertyConfigurationManager manager;
  private DynamicPropertyManager propertyManager;

  public PropertyServer() {
      manager = dynamicPropertyConfigurationManager();
      propertyManager = dynamicPropertyManager();
  }

  public DynamicPropertyManager dynamicPropertyManager() {
      ConversionService conversionService = new DefaultConversionService();
      PropertyExceptionHandler propertyExceptionHandler = new PropertyExceptionHandler(messageLookup());
      return new DynamicPropertyManager(conversionService, propertyExceptionHandler);
  }


  public DynamicPropertyConfigurationManager dynamicPropertyConfigurationManager() {
      DynamicPropertyConfigurationManager dynamicPropertyConfigurationManager = new DynamicPropertyConfigurationManager();

      PropertyConfiguration systemPropertyConfiguration = createSystemPropertyConfiguration();
      dynamicPropertyConfigurationManager.addConfigurationAtFront(systemPropertyConfiguration);

      PropertyConfiguration databasePropertyConfiguration = createDatabasePropertyConfiguration();
      dynamicPropertyConfigurationManager.addConfigurationAtFront(databasePropertyConfiguration);


      return dynamicPropertyConfigurationManager;
  }

    public MessageInterpolator messageInterpolator() {
        ResourceBundleLocator resourceBundleLocator = new MessageSourceResourceBundleLocator(messageSource());
        MessageInterpolator messageInterpolator = new ResourceBundleMessageInterpolator(resourceBundleLocator, false);
        MessageInterpolator localeMessageInterpolator = new LocaleContextMessageInterpolator(messageInterpolator);

        return localeMessageInterpolator;
    }

    public MessageLookup messageLookup() {
        return new MessageLookup(messageInterpolator(), messageSource());
    }

    public CoreResourceBundleMessageSource messageSource() {
        CoreResourceBundleMessageSource coreResourceBundleMessageSource = new CoreResourceBundleMessageSource();

        // UTF-8 must be set to fully support alternate languages and locales as ISO-8859-1 is insufficient.
        coreResourceBundleMessageSource.setDefaultEncoding(Charsets.UTF_8.toString());
        coreResourceBundleMessageSource.addBaseName("META-INF/i18n/core_validation_messages");
        coreResourceBundleMessageSource.addBaseName("META-INF/i18n/core_page_messages");
        coreResourceBundleMessageSource.addBaseName("META-INF/i18n/core_properties_messages");
        coreResourceBundleMessageSource.addBaseName("META-INF/i18n/core_security_messages");
        coreResourceBundleMessageSource.addBaseName("META-INF/i18n/core_messages");

        return coreResourceBundleMessageSource;
    }

 
  private ApplicationContext createContext() {
      GenericApplicationContext ctx = new GenericApplicationContext();
      PropertiesPostProcessor ppp = new PropertiesPostProcessor();
      ctx.addBeanFactoryPostProcessor(ppp);
      ctx.refresh();
      return ctx;
  }

  protected PropertyConfiguration createSystemPropertyConfiguration() {
      IPropertyConfigurationFactory propertyConfigurationFactory = new 
            SystemPropertyConfigurationFactory("SystemPropertyConfigurationPoller", springEnvironmentManager());

      final String configurationName = PropertyConfigurationName.SYSTEM.getName();
      Optional<PropertyConfiguration> propertyConfiguration = propertyConfigurationFactory.create(configurationName);
      if (!propertyConfiguration.isPresent()) {
          throw new BlackDuckServerException("System property configuration should be present.");
      }

      return propertyConfiguration.get();
  }

  private DatabasePolledConfigurationSource dbConfigSource;
  protected PropertyConfiguration createDatabasePropertyConfiguration() {
      IPropertyConfigurationFactory propertyConfigurationFactory = new 
            DatabaseConfigurationFactory("DatabaseConfigurationPoller", springEnvironmentManager());

      final String configurationName = "Database properties";
      Optional<PropertyConfiguration> propertyConfiguration = propertyConfigurationFactory.create(configurationName);
      if (!propertyConfiguration.isPresent()) {
          throw new BlackDuckServerException("Database property configuration should be present.");
      }
      dbConfigSource = ((DatabaseConfigurationFactory) propertyConfigurationFactory).getDbConfigSource();
      try {dbConfigSource.poll(true, null);}
      catch (Exception e) {
        
      }

      return propertyConfiguration.get();
  }

  protected SpringEnvironmentManager springEnvironmentManager() {
      SpringEnvironmentManager springEnvironmentManager = new SpringEnvironmentManager();
      springEnvironmentManager.initialize(createContext().getEnvironment());

      return springEnvironmentManager;
  }


  public static void main(String[] args) {
    try {
      String command = args[0];
      String propertyName = args[1];
      PropertyServer server = new PropertyServer();
      if (command.equals("set")) {
         String valueToSet = args[2];      
         server.set(propertyName, valueToSet);
      } else {
         String propVal = server.get(propertyName);
         System.out.println(propVal);
      }
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  public void set(String propertyName, String value) throws Exception {
    Connection conn = connectToDB("db");
    PreparedStatement ps = conn.prepareStatement("insert into properties (id, value) VALUES (?, ?)");
    ps.setString(1, propertyName);
    ps.setString(2, value);
    ps.executeUpdate(); 
    System.out.println(value);
  }

  public String get(String name) {
        final String defaultValue = "This is a default value.";
        DatabasePropertyConfiguration dbConfig = new DatabasePropertyConfiguration(dbConfigSource);
        sleep(500);
        String value = (String) dbConfig.getProperty(name);
        if (value != null) {
          return value;
        }
        Property<String> actualValue = propertyManager.getProperty(name, defaultValue);

        value = actualValue.getValue();
        return value;
  }

  Connection connectToDB(String host) throws SQLException {
    Connection conn = null;

    try {

      Class.forName("org.postgresql.Driver");
      String url = "jdbc:postgresql://" + host + "/postgres";

      while (conn == null) {
        try {
          conn = DriverManager.getConnection(url, "postgres", "");
        } catch (SQLException e) {
          System.err.println("Failed to connect to db - retrying");
          sleep(1000);
        }
      }

      PreparedStatement st = conn.prepareStatement(
        "CREATE TABLE IF NOT EXISTS properties (id VARCHAR(255) NOT NULL UNIQUE, value VARCHAR(255))");
      st.executeUpdate();

    } catch (ClassNotFoundException e) {
      e.printStackTrace();
      System.exit(1);
    }

    return conn;
  }
  static void sleep(long duration) {
    try {
      Thread.sleep(duration);
    } catch (InterruptedException e) {
      System.exit(1);
    }
  }
}
