package org.mandl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingHandler {

    private final Logger logger;

    private LoggingHandler(Class<?> clazz) {
        this.logger = LoggerFactory.getLogger(clazz);
    }

    public static LoggingHandler getLogger(Class<?> clazz) {
        return new LoggingHandler(clazz);
    }

    public void info(String message) {
        logger.info(message);
    }

    public void info(String message, Object... args) {
        logger.info(message, args);
    }

    public void warn(String message) {
        logger.warn(message);
    }

    public void warn(String message, Object... args) {
        logger.warn(message, args);
    }

    public void error(String message) {
        logger.error(message);
    }

    public void error(String message, Throwable throwable) {
        logger.error(message, throwable);
    }

    public void error(String message, Object... args) {
        logger.error(message, args);
    }

    public void debug(String message) {
        logger.debug(message);
    }

    public void debug(String message, Object... args) {
        logger.debug(message, args);
    }

    public void trace(String message) {
        logger.trace(message);
    }

    public void trace(String message, Object... args) {
        logger.trace(message, args);
    }
}

