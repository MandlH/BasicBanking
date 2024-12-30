package org.mandl;

import jakarta.enterprise.inject.se.SeContainer;
import jakarta.enterprise.inject.se.SeContainerInitializer;

public class Main {
    public static void main(String[] args) {

        LoggingHandler logger = LoggingHandler.getLogger(Main.class);

        try (SeContainer container = SeContainerInitializer.newInstance().initialize()) {
            logger.info("CDI container initialized.");

            ServiceManager serviceManager = container.select(ServiceManager.class).get();
            logger.info("Services retrieved successfully.");

            AuthenticationController authenticationController = new AuthenticationController(null, serviceManager);
            authenticationController.start(null, null);
        } catch (Exception e) {
            logger.error("An error occurred while initializing the CDI container or retrieving services.", e);
        }
    }
}