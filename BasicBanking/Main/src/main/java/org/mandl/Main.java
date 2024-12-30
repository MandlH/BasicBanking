package org.mandl;

import jakarta.enterprise.inject.se.SeContainer;
import jakarta.enterprise.inject.se.SeContainerInitializer;
import org.mandl.database.DatabaseConnection;
import org.mandl.repository.RepositoryWrapper;

public class Main {
    public static void main(String[] args) {

        LoggingHandler logger = LoggingHandler.getLogger(Main.class);
        try (SeContainer container = SeContainerInitializer.newInstance().initialize()) {
            logger.info("CDI container initialized.");
            ServiceManager serviceManager = container.select(ServiceManager.class).get();

            System.out.println("Initialize Database this could take some seconds.");

            DatabaseConnection db = container.select(DatabaseConnection.class).get();
            db.initializeSessionFactory();

            Controller authenticationController = ControllerFactory.getAuthenticationController(serviceManager);
            authenticationController.start();

        } catch (Exception e) {
            logger.error("An error occurred while initializing the CDI container or retrieving services.", e);
        }
    }
}