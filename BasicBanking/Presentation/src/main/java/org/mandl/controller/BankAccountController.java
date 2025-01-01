package org.mandl.controller;

import org.mandl.*;
import org.mandl.exceptions.ExceptionHandler;
import org.mandl.message.MessageHandler;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BankAccountController extends BaseController {

    private final String LIST_BANK_ACCOUNTS = "List Bank Accounts";
    private final String CREATE_BANK_ACCOUNT = "Create Bank Account";
    private final String DELETE_BANK_ACCOUNT = "Delete Bank Account";

    private BankAccountController(UserDto user, ServiceManager serviceManager) {
        super(user, serviceManager);
    }

    public static Controller create(UserDto user, ServiceManager serviceManager) {
        return new BankAccountController(user, serviceManager);
    }

    @Override
    protected Map<String, String> getOptions() {
        Map<String, String> options = new LinkedHashMap<>();
        options.put("1", LIST_BANK_ACCOUNTS);
        options.put("2", CREATE_BANK_ACCOUNT);
        options.put("3", DELETE_BANK_ACCOUNT);
        return options;
    }

    @Override
    protected String getMenuTitle() {
        return "Bank Accounts Management";
    }

    @Override
    protected void execute() {
        String action = getOptions().get(getLastInput());
        if (action == null) return;

        switch (action) {
            case LIST_BANK_ACCOUNTS -> listBankAccounts();
            case CREATE_BANK_ACCOUNT -> createBankAccount();
            case DELETE_BANK_ACCOUNT -> deleteBankAccount();
        }
    }

    private void listBankAccounts() {
        try {
            MessageHandler.printHeader(LIST_BANK_ACCOUNTS);
            List<BankAccountDto> bankAccounts = getServiceManager()
                    .getBankAccountService()
                    .getAllBankAccountsByOwnerId(getUser().getId());
            System.out.println("\nYour Bank Accounts:");
            bankAccounts.forEach(account -> System.out.println(account));
        } catch (Exception e) {
            ExceptionHandler.handleException(e);
        }
    }

    private void createBankAccount() {
        try {
            flushConsole();
            MessageHandler.printHeader(CREATE_BANK_ACCOUNT);
            displayPrompt("Enter Account Number: ");
            String accountNumber = getLastInput();
            displayPrompt("Enter Balance: ");
            String balanceInput = getLastInput().replace(",", ".");
            double balance = Double.parseDouble(balanceInput);

            BankAccountDto bankAccountDto = new BankAccountDto(
                    accountNumber, balance, BankAccountType.BUSINESS, getUser()
            );

            getServiceManager().getBankAccountService().createBankAccount(bankAccountDto);
            MessageHandler.printMessage("Bank account created successfully.");
        } catch (NumberFormatException e) {
            MessageHandler.printMessage("Invalid balance format. Please try again.");
        } catch (Exception e) {
            ExceptionHandler.handleException(e);
        }
    }

    private void deleteBankAccount() {
        MessageHandler.printHeader(DELETE_BANK_ACCOUNT);
    }
}
