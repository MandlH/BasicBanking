package org.mandl.controller;

import org.mandl.*;
import org.mandl.exceptions.ExceptionHandler;

public class AuthenticationController extends BaseController {

    final String LOGIN = "1";
    final String REGISTER = "2";

    private AuthenticationController(ServiceManager serviceManager) {
        super(null, serviceManager);
    }

    public static Controller create(ServiceManager serviceManager) {
        return new AuthenticationController(serviceManager);
    }

    @Override
    protected void execute() {
        switch (getLastInput().toLowerCase()) {
            case LOGIN:
                startLogin();
                break;
            case REGISTER:
                startRegister();
                break;
        }
    }

    @Override
    protected void displayActions() {
        flushConsole();
        System.out.println("\n============================");
        System.out.println("|          WELCOME         |");
        System.out.println("============================");
        System.out.println("| 1: Login                 |");
        System.out.println("| 2: Register              |");
        System.out.println("| back: Back               |");
        System.out.println("| exit: Exit Application   |");
        System.out.println("============================");
    }

    private void startLogin() {
        flushConsole();
        System.out.println("\n============================");
        System.out.println("|           Login          |");
        System.out.println("============================");
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
        System.out.println("\n============================");
        System.out.println("|         Register         |");
        System.out.println("============================");
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
