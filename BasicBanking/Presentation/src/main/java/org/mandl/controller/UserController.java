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

    private final String RESET_PASSWORD = "Reset Password";
    private final String DEACTIVATE_USER = "Deactivate User";
    private final String GET_USER_ACCOUNT_INFO = "User Account Info";

    public static Controller create(UserDto user, ServiceManager serviceManager) {
        return new UserController(user, serviceManager);
    }

    @Override
    protected Map<String, String> getOptions() {
        Map<String, String> options = new LinkedHashMap<>();
        options.put("1", RESET_PASSWORD);
        options.put("2", DEACTIVATE_USER);
        options.put("3", GET_USER_ACCOUNT_INFO);
        return options;
    }

    @Override
    protected String getMenuTitle() {
        return "User Action Management";
    }

    @Override
    protected void execute() {
        var action = getOptions().get(lastInput);
        if (action == null) return;

        switch (action) {
            case RESET_PASSWORD -> resetPassword();
            case DEACTIVATE_USER -> deleteUserAccount();
            case GET_USER_ACCOUNT_INFO -> getUserAccountInformation();
        }
    }

    private void resetPassword() {
        try {
            MessageHandler.printHeader(RESET_PASSWORD);
            printPrompt("Enter new password: ");
            String newPassword = lastInput;
            serviceManager.getIdentityUserService().resetPassword(newPassword);
            MessageHandler.printMessage("Password reset successfully.\n You have been logged out!");
            ControllerFactory.getAuthenticationController(serviceManager).start();
        } catch (Exception e) {
            ExceptionHandler.handleException(e);
        }
    }

    private void deleteUserAccount() {
        try {
            MessageHandler.printHeader(DEACTIVATE_USER);
            printPrompt("Enter your username to confirm deletion: ");
            String username = lastInput;
            if (username.equals(user.getUsername())) {
                serviceManager.getIdentityUserService().deactivateUser();
                MessageHandler.printMessage("Account deactivated successfully.\n You have been logged out!");
                ControllerFactory.getAuthenticationController(serviceManager).start();
                return;
            }
            MessageHandler.printMessage("Wrong username. Deactivation canceled.");
        } catch (Exception e) {
            ExceptionHandler.handleException(e);
        }
    }

    private void getUserAccountInformation() {
        try {
            MessageHandler.printHeader(GET_USER_ACCOUNT_INFO);
            List<BankAccountDto> bankAccounts = serviceManager.getBankAccountService().getAllBankAccounts();
            user.setBankAccounts(bankAccounts);
            MessageHandler.printMessage(user.toString());
        } catch (Exception e) {
            ExceptionHandler.handleException(e);
        }
    }
}
