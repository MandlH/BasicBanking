package org.mandl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoggingHandler {

    private final Logger logger;

    private LoggingHandler(Class<?> clazz) {
        this.logger = LogManager.getLogger(clazz);
    }

    public static LoggingHandler getLogger(Class<?> clazz) {
        return new LoggingHandler(clazz);
    }

    public void info(String message) {
        logger.info(message);
    }

    public void info(String message, Throwable throwable) {
        logger.info(message, throwable);
    }

    public void warn(String message, Throwable throwable) {
        logger.warn(message, throwable);
    }

    public void warn(String message) {
        logger.warn(message);
    }

    public void error(String message, Throwable throwable) {
        logger.error(message, throwable);
    }

    public void fatal(String message, Throwable throwable) {
        logger.fatal(message, throwable);
    }

    public void fatal(String message) {
        logger.fatal(message);
    }
}
