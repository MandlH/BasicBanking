package org.mandl.controller;

import org.mandl.*;

import java.util.LinkedHashMap;
import java.util.Map;

public class NavigationController extends BaseController {

    private final String BANK_ACCOUNTS_MANAGEMENT = "Bank Accounts Management";
    private final String TRANSACTION_MANAGEMENT = "Transaction Management";
    private final String USER_ACCOUNTS_MANAGEMENT = "User Accounts Management";
    private final String LOGOUT = "Logout";

    private NavigationController(UserDto user, ServiceManager serviceManager) {
        super(user, serviceManager);
    }

    public static Controller create(UserDto user, ServiceManager serviceManager) {
        return new NavigationController(user, serviceManager);
    }

    @Override
    protected Map<String, String> getOptions() {
        Map<String, String> options = new LinkedHashMap<>();
        options.put("1", BANK_ACCOUNTS_MANAGEMENT);
        options.put("2", TRANSACTION_MANAGEMENT);
        options.put("3", USER_ACCOUNTS_MANAGEMENT);
        options.put("4", LOGOUT);
        return options;
    }

    @Override
    protected String getMenuTitle() {
        return "Navigation";
    }

    @Override
    protected void execute() {
        var action = getOptions().get(lastInput);
        if (action == null) return;

        switch (action) {
            case BANK_ACCOUNTS_MANAGEMENT -> ControllerFactory.getBankAccountController(user, serviceManager).start();
            case TRANSACTION_MANAGEMENT -> ControllerFactory.getTransactionController(user, serviceManager).start();
            case USER_ACCOUNTS_MANAGEMENT -> ControllerFactory.getUserController(user, serviceManager).start();
            case LOGOUT -> ControllerFactory.getAuthenticationController(serviceManager).start();
        }
    }
}
