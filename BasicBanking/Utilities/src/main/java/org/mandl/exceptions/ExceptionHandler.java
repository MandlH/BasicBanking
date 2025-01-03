package org.mandl.exceptions;

import org.mandl.message.MessageHandler;

public class ExceptionHandler {

    public static void handleException(Exception e) {
        switch (e) {
            case AuthenticationException authEx ->
                    MessageHandler.printWarning(authEx);
            case IllegalArgumentException illegalArgEx ->
                    MessageHandler.printInfo(illegalArgEx);
            default ->
                    MessageHandler.printError("An unexpected error occurred", e);
        }
    }
}