package org.mandl.exceptions;

import org.mandl.LoggingHandler;

public class ExceptionHandler {
    private static final LoggingHandler logger = LoggingHandler.getLogger(ExceptionHandler.class);

    public static void handleException(Exception e) {
        if (e instanceof AuthenticationException) {
            System.out.println("-".repeat(9 + e.getMessage().length()));
            System.out.println("|Error: " + e.getMessage() + "|");
            System.out.println("-".repeat(9 + e.getMessage().length()));
            logger.info(e.getMessage());
        } else {
            System.out.println("-".repeat(45));
            System.out.println("|Error: An unexpected error occurred|");
            System.out.println("-".repeat(45));
            logger.error(e.getMessage());
        }
    }
}
