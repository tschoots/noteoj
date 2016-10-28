package com.blackducksoftware.core.properties.configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import javax.annotation.PreDestroy;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.netflix.config.AbstractPollingScheduler;

/**
 * Property polling scheduler. A mechanism for polling a property source for updated based on a fixed delay cycle.
 * 
 * @author skatzman
 * 
 */
public class PropertyPollingScheduler extends AbstractPollingScheduler {
    public static final long DEFAULT_INITIAL_DELAY_MILLIS = 0L;

    public static final long DEFAULT_DELAY_MILLIS = 30000L;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final long initialDelayMillis;

    private final long delayMillis;

    private final String name;

    private ScheduledExecutorService scheduledExecutorService;

    /**
     * Default constructor.
     * 
     * Default values for initial delay in milliseconds and delay in milliseconds will be used.
     * 
     * @param name
     *            The poller name. Must not be blank.
     * @see PropertyPollingScheduler.DEFAULT_INITIAL_DELAY_MILLIS
     * @see PropertyPollingScheduler.DEFAULT_DELAY_MILLIS
     */
    public PropertyPollingScheduler(String name) {
        this(name, DEFAULT_INITIAL_DELAY_MILLIS, DEFAULT_DELAY_MILLIS);
    }

    /**
     * Main constructor.
     * 
     * @param name
     *            The poller name. Must not be blank.
     * @param initialDelayMillis
     *            The initial delay in milliseconds. Must be greater than or equal to 0.
     * @param delayMillis
     *            The delay in milliseconds. Must be greater than 0.
     */
    public PropertyPollingScheduler(String name, long initialDelayMillis, long delayMillis) {
        Preconditions.checkArgument(StringUtils.isNotBlank(name), "Name must not be blank.");
        Preconditions.checkArgument(initialDelayMillis >= 0, "Initial delay in milliseconds must be greater than or equal to 0.");
        Preconditions.checkArgument(delayMillis > 0, "Delay in milliseconds must be greater than or equal to 0.");

        this.name = name;
        this.initialDelayMillis = initialDelayMillis;
        this.delayMillis = delayMillis;
    }

    @PreDestroy
    public void destroy() {
        stop();
    }

    @Override
    public void stop() {
        if (scheduledExecutorService != null) {
            logger.info("Stopping property polling scheduler [Initial delay: {} | Delay: {}]", initialDelayMillis, delayMillis);

            scheduledExecutorService.shutdown();
            scheduledExecutorService = null;
        }
    }

    /**
     * Gets the initial delay in milliseconds.
     * 
     * @return long Returns the initial delay in milliseconds.
     */
    public long getInitialDelayMillis() {
        return initialDelayMillis;
    }

    /**
     * Gets the delay in milliseconds.
     * 
     * @return long Returns the delay in milliseconds.
     */
    public long getDelayMillis() {
        return delayMillis;
    }

    /**
     * Determines if the polling scheduler is shutdown.
     * 
     * @return boolean Returns true if the polling scheduler is shutdown and false otherwise.
     */
    public boolean isShutdown() {
        if (scheduledExecutorService != null) {
            return scheduledExecutorService.isShutdown();
        }

        return true;
    }

    @Override
    protected void schedule(Runnable pollingRunnable) { // NOPMD DoNotUseThreads
        scheduledExecutorService = Executors.newScheduledThreadPool(1, new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) { // NOPMD DoNotUseThreads
                Thread t = new Thread(r, name); // NOPMD DoNotUseThreads
                t.setDaemon(true);
                return t;
            }
        });

        logger.info("Scheduling property polling scheduler [Initial delay: {} | Delay: {}]", initialDelayMillis, delayMillis);

        scheduledExecutorService.scheduleWithFixedDelay(pollingRunnable, initialDelayMillis, delayMillis, TimeUnit.MILLISECONDS);
    }
}
