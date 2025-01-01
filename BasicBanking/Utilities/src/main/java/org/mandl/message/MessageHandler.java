package org.mandl.message;

import org.mandl.LoggingHandler;
import org.mandl.exceptions.ExceptionHandler;

import java.util.Scanner;

public class MessageHandler {
    private static final Scanner scanner = new Scanner(System.in);
    private static final LoggingHandler logger = LoggingHandler.getLogger(ExceptionHandler.class);

    public static void displayMessage(String message) {
        int length = 7 + message.length();
        System.out.println("-".repeat(length));
        System.out.println("Message: " + message);
        System.out.println("Press Enter to continue...");
        System.out.println("-".repeat(length));
        scanner.nextLine();
        logger.info(message);
    }

    public static void displayError(String errorMessage) {
        int length = 7 + errorMessage.length();
        System.out.println("-".repeat(length));
        System.out.println("Error: " + errorMessage);
        System.out.println("Press Enter to continue...");
        System.out.println("-".repeat(length));
        scanner.nextLine();
    }

    public static void displayError(String errorMessage, Exception e) {
        logger.error(e.getMessage());
        displayError(errorMessage);
    }
}
