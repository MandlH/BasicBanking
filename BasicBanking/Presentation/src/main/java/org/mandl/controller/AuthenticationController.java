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
        return new AuthenticationController(serviceManager);
    }

    @Override
    protected void execute() {
        String action = getOptions().get(getLastInput());
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
        flushConsole();
        MessageHandler.printHeader(LOGIN);
        displayPrompt("Enter Username: ");
        String username = getLastInput();
        displayPrompt("Enter Password: ");
        String password = getLastInput();
        validateLogin(username, password);
    }

    private void validateLogin(String username, String password) {
        try {
            UserDto user = getServiceManager().getIdentityUserService().loginUser(username, password);
            Controller navigationController = ControllerFactory.getNavigationController(user, getServiceManager());
            navigationController.start();
        } catch (Exception e) {
            ExceptionHandler.handleException(e);
        }
    }

    private void startRegister() {
        flushConsole();
        MessageHandler.printHeader(REGISTER);
        displayPrompt("Enter Username: ");
        String username = getLastInput();
        displayPrompt("Enter Password: ");
        String password = getLastInput();
        validateRegister(username, password);
    }

    private void validateRegister(String username, String password) {
        try {
            UserDto user = getServiceManager().getIdentityUserService().registerUser(username, password);
            Controller navigationController = ControllerFactory.getNavigationController(user, getServiceManager());
            navigationController.start();
        } catch (Exception e) {
            ExceptionHandler.handleException(e);
        }
    }
}
