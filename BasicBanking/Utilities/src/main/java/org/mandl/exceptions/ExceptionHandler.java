package org.mandl.exceptions;

import org.mandl.LoggingHandler;

public class ExceptionHandler {
    private static final LoggingHandler logger = LoggingHandler.getLogger(ExceptionHandler.class);

    public static void handleException(Exception e) {
        if (e instanceof AuthenticationException) {
            System.out.println("Error: " + e.getMessage());
            logger.info(e.getMessage());
        } else {
            System.out.println("An unexpected error occurred");
            logger.error(e.getMessage());
        }
    }
}
