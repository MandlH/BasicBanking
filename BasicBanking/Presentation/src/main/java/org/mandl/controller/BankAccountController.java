package org.mandl.controller;

import org.mandl.*;
import org.mandl.exceptions.ExceptionHandler;
import org.mandl.message.MessageHandler;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;

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
            var bankAccounts = getServiceManager()
                    .getBankAccountService()
                    .getAllBankAccountsByOwnerId(getUser().getId());

            var message = Stream.concat(
                            Stream.of(String.format(
                                    "| %-15s | %-12s | %-10s |",
                                    "Account No.", "Balance", "Type"
                            )),
                            bankAccounts.stream()
                                    .map(BankAccountDto::toString)
                    )
                    .toList();

            MessageHandler.printMessages(message);
        } catch (Exception e) {
            ExceptionHandler.handleException(e);
        }
    }

    private void createBankAccount() {
        try {
            flushConsole();
            MessageHandler.printHeader(CREATE_BANK_ACCOUNT);

            printPrompt("Enter Account Number: ");
            var accountNumber = getLastInput();

            StringBuilder typeOptions = new StringBuilder("Enter Type (");
            for (var type : BankAccountTypeDto.values()) {
                typeOptions.append(type).append(", ");
            }
            typeOptions.setLength(typeOptions.length() - 2); // Remove trailing ", "
            typeOptions.append("): ");
            printPrompt(typeOptions.toString());
            var typeInput = getLastInput().toUpperCase();

            BankAccountTypeDto accountType;
            try {
                accountType = BankAccountTypeDto.valueOf(typeInput);
            } catch (IllegalArgumentException e) {
                MessageHandler.printMessage("Invalid account type. Please try again.");
                return;
            }

            printPrompt("Enter Balance: ");
            String balanceInput = getLastInput().replace(",", ".");
            double balance = Double.parseDouble(balanceInput);

            BankAccountDto bankAccountDto = new BankAccountDto(
                    accountNumber, balance, accountType, getUser()
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
        try {
            MessageHandler.printHeader(DELETE_BANK_ACCOUNT);
            printPrompt("Enter Account Number: ");
            var accountNumber = getLastInput();
            getServiceManager().getBankAccountService().deleteBankAccount(accountNumber);
            MessageHandler.printMessage("Bank account deleted successfully.");
        } catch (Exception e) {
            ExceptionHandler.handleException(e);
        }
    }
}
