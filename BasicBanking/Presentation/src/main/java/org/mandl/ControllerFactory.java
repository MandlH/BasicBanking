package org.mandl;

import org.mandl.controller.AuthenticationController;
import org.mandl.controller.BankAccountController;
import org.mandl.controller.NavigationController;
import org.mandl.controller.UserController;

public class ControllerFactory {
    public static Controller getAuthenticationController(ServiceManager serviceManager) {
        return AuthenticationController.create(serviceManager);
    }

    public static Controller getNavigationController(UserDto user, ServiceManager serviceManager) {
        return NavigationController.create(user, serviceManager);
    }

    public static Controller getUserController(UserDto user, ServiceManager serviceManager) {
        return UserController.create(user, serviceManager);
    }

    public static Controller getBankAccountController(UserDto user, ServiceManager serviceManager) {
        return BankAccountController.create(user, serviceManager);
    }
}
