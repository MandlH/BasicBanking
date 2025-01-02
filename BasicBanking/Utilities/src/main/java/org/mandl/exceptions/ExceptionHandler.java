package org.mandl.exceptions;

import org.mandl.message.MessageHandler;

public class ExceptionHandler {

    public static void handleException(Exception e) {
        if (e instanceof AuthenticationException) {
            MessageHandler.printError(e.getMessage());
        } else {
            MessageHandler.printError("An unexpected error occurred", e);
        }
    }
}