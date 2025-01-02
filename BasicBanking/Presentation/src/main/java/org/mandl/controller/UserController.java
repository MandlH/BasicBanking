package org.mandl.controller;

import org.mandl.*;
import org.mandl.exceptions.ExceptionHandler;
import org.mandl.message.MessageHandler;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class UserController extends BaseController {

    private UserController(UserDto user, ServiceManager serviceManager) {
        super(user, serviceManager);
    }

    public static Controller create(UserDto user, ServiceManager serviceManager) {
        return new UserController(user, serviceManager);
    }

    @Override
    protected Map<String, String> getOptions() {
        Map<String, String> options = new LinkedHashMap<>();
        options.put("1", "Reset Password");
        options.put("2", "Delete User Account");
        options.put("3", "Get User Account Info");
        return options;
    }

    @Override
    protected String getMenuTitle() {
        return "User Action Management";
    }

    @Override
    protected void execute() {
        String action = getOptions().get(getLastInput());
        if (action == null) return;

        switch (action) {
            case "Reset Password" -> resetPassword();
            case "Delete User Account" -> deleteUserAccount();
            case "Get User Account Info" -> getUserAccountInformation();
        }
    }

    private void resetPassword() {
        try {
            flushConsole();
            MessageHandler.printHeader("Reset Password");
            printPrompt("Enter new password: ");
            String newPassword = getLastInput();
            getServiceManager().getIdentityUserService().resetPassword(getUser().getId(), newPassword);
            MessageHandler.printMessage("Password reset successfully.\n You have been logged out!");
            ControllerFactory.getAuthenticationController(getServiceManager()).start();
        } catch (Exception e) {
            ExceptionHandler.handleException(e);
        }
    }

    private void deleteUserAccount() {
        try {
            flushConsole();
            MessageHandler.printHeader("Delete User Account");
            printPrompt("Enter your username to confirm deletion: ");
            String username = getLastInput();
            if (username.equals(getUser().getUsername())) {
                getServiceManager().getIdentityUserService().delete(getUser().getId());
                MessageHandler.printMessage("Account deleted successfully.\n You have been logged out!");
                ControllerFactory.getAuthenticationController(getServiceManager()).start();
                return;
            }
            MessageHandler.printMessage("Wrong username. Deletion canceled.");
        } catch (Exception e) {
            ExceptionHandler.handleException(e);
        }
    }

    private void getUserAccountInformation() {
        try {
            flushConsole();
            MessageHandler.printHeader("User Account Information");
            UserDto user = getServiceManager().getIdentityUserService().getUser(getUser().getId());
            List<BankAccountDto> bankAccounts = getServiceManager().getBankAccountService().getAllBankAccountsByOwnerId(getUser().getId());
            user.setBankAccounts(bankAccounts);
            MessageHandler.printMessage(user.toString());
        } catch (Exception e) {
            ExceptionHandler.handleException(e);
        }
    }
}
