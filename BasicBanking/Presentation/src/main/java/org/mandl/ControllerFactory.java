package org.mandl;

import org.mandl.controller.*;

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

    public static Controller getTransactionController(UserDto user, ServiceManager serviceManager) {
        return TransactionController.create(user, serviceManager);
    }
}
