package com.blackducksoftware.core.properties.configuration.writeable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.postgresql.jdbc3.Jdbc3SimpleDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.netflix.config.PollResult;
import com.netflix.config.PolledConfigurationSource;

/**
 * The system property polled configuration source. Periodically called by the property infrastructure to return a poll
 * result containing the full map of system property key/value pairs.
 * 
 * @author jgamache
 * 
 */
public class DatabasePolledConfigurationSource implements PolledConfigurationSource {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final Jdbc3SimpleDataSource jdbcDataSource;

    private Map<String, Object> databasePropertyMap = Maps.newHashMap();
    
	/**
     * Default constructor.
     */
    public DatabasePolledConfigurationSource() {
    	jdbcDataSource = new Jdbc3SimpleDataSource();
    	jdbcDataSource.setDatabaseName("postgres");
    	jdbcDataSource.setServerName("db");
    	jdbcDataSource.setUser("postgres");
    }
    
    public Map<String, Object> getDatabasePropertyMap() {
		return databasePropertyMap;
	}


    @Override
    public PollResult poll(boolean initial, Object checkPoint) throws Exception { // NOPMD
                                                                                  // SignatureDeclareThrowsException
        // 3rd-party library abstract method, must throw Exception.

        // Return a full result of all current, supported system property values.
        logger.info("Attempting to poll for database values.");

        databasePropertyMap = Maps.newHashMap();

        Connection connection = jdbcDataSource.getConnection();
        PreparedStatement st = connection.prepareStatement(
        		"SELECT * FROM properties");
        ResultSet rs = st.executeQuery();
        while (rs.next()) {
        	String propName = rs.getString(1);
        	String propVal  = rs.getString(2);
        	databasePropertyMap.put(propName, propVal);
        }

        if (logger.isDebugEnabled()) {
            final String threadName = Thread.currentThread().getName();
            logger.debug("Successfully polled for database property values [Number of properties: {} | Thread name: {}].", databasePropertyMap.size(), threadName);
        }

        return PollResult.createFull(databasePropertyMap);
    }
}
