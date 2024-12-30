package org.mandl;

import org.mandl.exceptions.ExceptionHandler;

public class UserController extends BaseController {

    final String RESET_PASSWORD = "1";
    final String DELETE_USER_ACCOUNT = "2";

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
        }
    }

    @Override
    protected void displayActions() {
        flushConsole();
        System.out.println("\n================================");
        System.out.println("|     User Account Management     |");
        System.out.println("=================================");
        System.out.println("| 1: Reset Password             |");
        System.out.println("| 2: Delete User Account        |");
        System.out.println("| back: Back                    |");
        System.out.println("| exit: Exits Application.      |");
        System.out.println("=================================");
    }

    private void resetPassword() {
        try{
            displayPrompt("Enter new password: ");
            String newPassword = getLastInput();
            serviceManager.getIdentityUserService().resetPassword(getUser().getId(), newPassword);
            ControllerFactory.getAuthenticationController(serviceManager).start(null);
        } catch (Exception e) {
            ExceptionHandler.handleException(e);
        }
    }

    private void deleteUserAccount() {
        try {
            displayPrompt("Enter Username to Delete Account: ");
            String username = getLastInput();
            if (username.equals(getUser().getUsername())) {
                serviceManager.getIdentityUserService().delete(getUser().getId());
            }
            ControllerFactory.getAuthenticationController(serviceManager).start(null);
        } catch (Exception e) {
            ExceptionHandler.handleException(e);
        }
    }
}