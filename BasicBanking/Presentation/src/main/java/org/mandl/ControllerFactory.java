package org.mandl;

public class ControllerFactory {
    public static Controller getAuthenticationController(ServiceManager serviceManager) {
        return AuthenticationController.create(serviceManager);
    }

    public static Controller getNavigationController(UserDto user, ServiceManager serviceManager) {
        return NavigationController.create(user, serviceManager);
    }
}