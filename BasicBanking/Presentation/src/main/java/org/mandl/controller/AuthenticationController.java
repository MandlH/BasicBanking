package org.mandl.controller;

import org.mandl.*;
import org.mandl.exceptions.ExceptionHandler;
import org.mandl.message.MessageHandler;

import java.util.LinkedHashMap;
import java.util.Map;

public class AuthenticationController extends BaseController {

    private final String LOGIN = "Login";
    private final String REGISTER = "Register";

    private AuthenticationController(ServiceManager serviceManager) {
        super(null, serviceManager);
    }

    public static Controller create(ServiceManager serviceManager) {
        serviceManager.getAuthenticationService().logoutUser();
        return new AuthenticationController(serviceManager);
    }

    @Override
    protected void execute() {
        String action = getOptions().get(lastInput);
        if (action == null) return;

        switch (action) {
            case LOGIN -> startLogin();
            case REGISTER -> startRegister();
        }
    }

    @Override
    protected Map<String, String> getOptions() {
        Map<String, String> options = new LinkedHashMap<>();
        options.put("1", LOGIN);
        options.put("2", REGISTER);
        return options;
    }

    @Override
    protected String getMenuTitle() {
        return "Authentication";
    }

    private void startLogin() {
        try {
            MessageHandler.printHeader(LOGIN);
            printPrompt("Enter Username: ");
            var username = lastInput;
            printPrompt("Enter Password: ");
            var password = lastInput;
            var user = serviceManager.getAuthenticationService().loginUser(username, password);
            var navigationController = ControllerFactory.getNavigationController(user, serviceManager);
            navigationController.start();
        } catch (Exception ex) {
            ExceptionHandler.handleException(ex);
        }
    }

    private void startRegister() {
        try {
            MessageHandler.printHeader(REGISTER);
            printPrompt("Enter Username: ");
            var username = lastInput;
            printPrompt("Enter Password: ");
            var password = lastInput;
            var user = serviceManager.getAuthenticationService().registerUser(username, password);
            var navigationController = ControllerFactory.getNavigationController(user, serviceManager);
            navigationController.start();
        } catch (Exception e) {
            ExceptionHandler.handleException(e);
        }
    }
}
