package org.mandl;

import jakarta.enterprise.inject.se.SeContainer;
import jakarta.enterprise.inject.se.SeContainerInitializer;

public class Main {
    public static void main(String[] args) {
        System.setProperty("org.jboss.weld.debug", "true");

        try (SeContainer container = SeContainerInitializer.newInstance().initialize()) {
            System.out.println("CDI container initialized.");

            // Resolve ServiceManager
            ServiceManager serviceManager = container.select(ServiceManager.class).get();
            System.out.println("ServiceManager resolved: " + serviceManager);

            // Use the ServiceManager to get IdentityUserService
            IdentityUserService userService = serviceManager.getIdentityUserService();
            System.out.println("IdentityUserService resolved: " + userService);

            // Test user registration and retrieval
            UserDto user = new UserDto("Test6", "Test6", "Password");
            userService.registerUser(user);
            System.out.println("User registered: " + user.getUserName());

            UserDto retrievedUser = userService.getUser("Test.Test");
            System.out.println("Retrieved user: " + retrievedUser.getUserName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}