package org.mandl.controller;

import jdk.jshell.spi.ExecutionControl;
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
        String action = getOptions().get(getLastInput());
        if (action == null) return;

        switch (action) {
            case BANK_ACCOUNTS_MANAGEMENT -> ControllerFactory.getBankAccountController(getUser(), getServiceManager()).start();
            case TRANSACTION_MANAGEMENT -> {
                try {
                    throw new ExecutionControl.NotImplementedException("asdf");
                } catch (ExecutionControl.NotImplementedException e) {
                    throw new RuntimeException(e);
                }
            }
            case USER_ACCOUNTS_MANAGEMENT -> ControllerFactory.getUserController(getUser(), getServiceManager()).start();
            case LOGOUT -> ControllerFactory.getAuthenticationController(getServiceManager()).start();
        }
    }
}
