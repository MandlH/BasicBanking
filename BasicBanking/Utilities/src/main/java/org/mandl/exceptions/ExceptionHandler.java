package org.mandl.exceptions;

import org.mandl.message.MessageHandler;

import java.util.Scanner;

public class ExceptionHandler {

    private static final Scanner scanner = new Scanner(System.in);

    public static void handleException(Exception e) {
        if (e instanceof AuthenticationException) {
            MessageHandler.displayError(e.getMessage());
        } else {
            MessageHandler.displayError("An unexpected error occurred", e);
        }
    }
}
