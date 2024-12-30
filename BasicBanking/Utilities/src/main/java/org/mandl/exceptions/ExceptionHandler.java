package org.mandl.exceptions;

import org.mandl.LoggingHandler;

import java.util.Scanner;

public class ExceptionHandler {
    private static final LoggingHandler logger = LoggingHandler.getLogger(ExceptionHandler.class);
    private static final Scanner scanner = new Scanner(System.in);

    public static void handleException(Exception e) {
        if (e instanceof AuthenticationException) {
            System.out.println("-".repeat(7 + e.getMessage().length()));
            System.out.println("Error: " + e.getMessage());
            System.out.println("Press Enter to continue...");
            System.out.println("-".repeat(7 + e.getMessage().length()));
            scanner.nextLine();
            logger.info(e.getMessage());
        } else {
            System.out.println("-".repeat(45));
            System.out.println("Error: An unexpected error occurred");
            System.out.println("Press Enter to continue...");
            System.out.println("-".repeat(45));
            scanner.nextLine();
            logger.error(e.getMessage());
        }
    }
}
