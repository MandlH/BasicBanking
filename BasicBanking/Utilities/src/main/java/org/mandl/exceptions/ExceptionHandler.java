package org.mandl.exceptions;

import org.mandl.message.MessageHandler;

public class ExceptionHandler {

    public static void handleException(Exception e) {
        switch (e) {
            case AuthenticationException authEx ->
                    MessageHandler.printError(authEx.getMessage());
            case IllegalArgumentException illegalArgEx ->
                    MessageHandler.printError(illegalArgEx.getMessage());
            default ->
                    MessageHandler.printError("An unexpected error occurred", e);
        }
    }
}