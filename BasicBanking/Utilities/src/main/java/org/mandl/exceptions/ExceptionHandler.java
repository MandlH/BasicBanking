package org.mandl.exceptions;

import org.mandl.message.MessageHandler;

public class ExceptionHandler {

    public static void handleException(Exception e) {
        if (e instanceof AuthenticationException) {
            MessageHandler.displayError(e.getMessage());
        } else {
            MessageHandler.displayError("An unexpected error occurred", e);
        }
    }
}
