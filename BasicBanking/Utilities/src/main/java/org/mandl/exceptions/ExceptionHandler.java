package org.mandl.exceptions;

import org.hibernate.service.spi.ServiceException;
import org.mandl.message.MessageHandler;

public class ExceptionHandler {

    public static void handleException(Exception e) {
        switch (e) {
            case AuthenticationException authenticationException ->
                    MessageHandler.printWarning(authenticationException);
            case IllegalArgumentException illegalArgumentException ->
                    MessageHandler.printInfo(illegalArgumentException);
            case ServiceException serviceException ->
                    MessageHandler.printInfo(serviceException);
            default ->
                    MessageHandler.printError("An unexpected error occurred", e);
        }
    }
}