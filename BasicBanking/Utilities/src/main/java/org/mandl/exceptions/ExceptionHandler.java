package org.mandl.exceptions;

import org.hibernate.service.spi.ServiceException;
import org.mandl.LoggingHandler;
import org.mandl.message.MessageHandler;

public class ExceptionHandler {

    public static void handleException(Exception e) {
        if (e == null) {
            LoggingHandler logger = LoggingHandler.getLogger(ExceptionHandler.class);
            logger.fatal("Attempted to handle a null exception.");
            MessageHandler.printExceptionMessage("An unknown error occurred.");
            return;
        }

        Class<?> originatingClass = getOriginatingClass(e);
        LoggingHandler logger = LoggingHandler.getLogger(originatingClass);

        switch (e) {
            case AuthenticationException authenticationException -> {
                var message = authenticationException.getMessage();
                if (authenticationException.getCause() != null) {
                    logger.fatal(message, authenticationException);
                }

                MessageHandler.printExceptionMessage(message);
            }
            case IllegalArgumentException illegalArgumentException -> {
                var message = illegalArgumentException.getMessage();
                if (illegalArgumentException.getCause() != null) {
                    logger.warn(message, illegalArgumentException);
                }

                MessageHandler.printExceptionMessage(message);
            }
            case ServiceException serviceException -> {
                var message = serviceException.getMessage();
                if (serviceException.getCause() != null) {
                    logger.error(message, serviceException);
                }

                MessageHandler.printExceptionMessage(message);
            }
            default -> {
                var message = e.getMessage();
                logger.fatal(message, e);
                MessageHandler.printExceptionMessage("Unexpected error occurred.");
            }
        }
    }

    private static Class<?> getOriginatingClass(Exception e) {
        StackTraceElement[] stackTrace = e.getStackTrace();
        if (stackTrace.length > 0) {
            try {
                return Class.forName(stackTrace[0].getClassName());
            } catch (ClassNotFoundException ex) {
                LoggingHandler logger = LoggingHandler.getLogger(ExceptionHandler.class);
                logger.error("Could not find class for originating exception: " + stackTrace[0].getClassName(), ex);
            }
        }
        return ExceptionHandler.class;
    }
}
