package org.mandl.controller;

import org.mandl.*;
import org.mandl.exceptions.ExceptionHandler;
import org.mandl.message.MessageHandler;

public class UserController extends BaseController {

    final String RESET_PASSWORD = "1";
    final String DELETE_USER_ACCOUNT = "2";
    final String USER_ACCOUNT_INFORMATION = "3";

    private UserController(UserDto user, ServiceManager serviceManager) {
        super(user, serviceManager);
    }

    public static Controller create(UserDto user, ServiceManager serviceManager) {
        return new UserController(user, serviceManager);
    }

    @Override
    protected void execute() {
        switch (getLastInput().toLowerCase()) {
            case RESET_PASSWORD:
                resetPassword();
                break;
            case DELETE_USER_ACCOUNT:
                deleteUserAccount();
                break;
            case USER_ACCOUNT_INFORMATION:
                getUserAccountInformation();
                break;
        }
    }

    @Override
    protected void displayActions() {
        flushConsole();
        System.out.println("\n================================");
        System.out.println("|    User Account Management    |");
        System.out.println("=================================");
        System.out.println("| 1: Reset Password             |");
        System.out.println("| 2: Delete User Account        |");
        System.out.println("| 3: Get User Account Infos     |");
        System.out.println("| back: Back                    |");
        System.out.println("| exit: Exits Application.      |");
        System.out.println("=================================");
    }

    private void resetPassword() {
        try{
            displayPrompt("Enter new password: ");
            String newPassword = getLastInput();
            getServiceManager().getIdentityUserService().resetPassword(getUser().getId(), newPassword);
            ControllerFactory.getAuthenticationController(getServiceManager()).start();
        } catch (Exception e) {
            ExceptionHandler.handleException(e);
        }
    }

    private void deleteUserAccount() {
        try {
            displayPrompt("Enter Username to Delete Account: ");
            String username = getLastInput();
            if (username.equals(getUser().getUsername())) {
                getServiceManager().getIdentityUserService().delete(getUser().getId());
                ControllerFactory.getAuthenticationController(getServiceManager()).start();
                return;
            }

            MessageHandler.displayMessage("Wrong Username, deletion was canceled.");
        } catch (Exception e) {
            ExceptionHandler.handleException(e);
        }
    }

    private void getUserAccountInformation() {
        try {
            UserDto user = getServiceManager().getIdentityUserService().getUser(getUser().getId());
            System.out.println(user.toString());
            displayPrompt("Press Enter to continue: ");
        } catch (Exception e) {
            ExceptionHandler.handleException(e);
        }
    }
}
