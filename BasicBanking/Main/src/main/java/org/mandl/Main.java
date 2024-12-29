package org.mandl;

import jakarta.enterprise.inject.se.SeContainer;
import jakarta.enterprise.inject.se.SeContainerInitializer;

public class Main {
    public static void main(String[] args) {
        LoggingHandler logger = LoggingHandler.getLogger(Main.class);

        try (SeContainer container = SeContainerInitializer.newInstance().initialize()) {
            ServiceManager serviceManager = container.select(ServiceManager.class).get();
            IdentityUserService userService = serviceManager.getIdentityUserService();
            logger.info("CDI container initialized.");
        } catch (Exception e) {
            logger.error("An error occurred while initializing the CDI container or retrieving services.", e);
        }
    }
}