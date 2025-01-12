package org.mandl.controller;

import org.mandl.*;
import org.mandl.exceptions.ExceptionHandler;
import org.mandl.message.MessageHandler;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BankAccountController extends BaseController {

    private final String LIST_BANK_ACCOUNTS = "List Bank Accounts";
    private final String CREATE_BANK_ACCOUNT = "Create Bank Account";
    private final String DELETE_BANK_ACCOUNT = "Deactivate Bank Account";

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
        var action = getOptions().get(lastInput);
        if (action == null) return;

        switch (action) {
            case LIST_BANK_ACCOUNTS -> listBankAccounts();
            case CREATE_BANK_ACCOUNT -> createBankAccount();
            case DELETE_BANK_ACCOUNT -> deleteBankAccount();
        }
    }

    private void listBankAccounts() {
        MessageHandler.printHeader(LIST_BANK_ACCOUNTS);
        try {
            var bankAccounts = serviceManager
                    .getBankAccountService()
                    .getAllBankAccounts();

            PartialController.PrintBankAccounts(bankAccounts, true);
        } catch (Exception e) {
            ExceptionHandler.handleException(e);
        }
    }

    private void createBankAccount() {

            MessageHandler.printHeader(CREATE_BANK_ACCOUNT);

            printPrompt("Enter Account Number: ");
            var accountNumber = lastInput;

            var typeOptions = new StringBuilder("Enter Type (");
            for (var type : BankAccountTypeDto.values()) {
                typeOptions.append(type).append(", ");
            }
            typeOptions.setLength(typeOptions.length() - 2);
            typeOptions.append("): ");
            printPrompt(typeOptions.toString());
            var typeInput = lastInput.toUpperCase();

            BankAccountTypeDto accountType;
            try {
                accountType = BankAccountTypeDto.valueOf(typeInput);
            } catch (IllegalArgumentException e) {
                MessageHandler.printMessage("Invalid account type. Please try again.");
                return;
            }

        try {
            printPrompt("Enter Balance: ");
            var balanceInput = lastInput.replace(",", ".");
            var balance = new BigDecimal(balanceInput);

            BankAccountDto bankAccountDto = new BankAccountDto(
                    accountNumber, balance, accountType, user
            );

            serviceManager.getBankAccountService().createBankAccount(bankAccountDto);
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
            var bankAccounts = serviceManager.getBankAccountService().getAllBankAccounts();
            PartialController.PrintBankAccounts(bankAccounts, false);
            System.out.println("Deactivated Bank Accounts can not be reactivated and are not accessible!");
            System.out.println("Transactions are not possible with deactivated bank accounts.");
            printPrompt("Enter Account Number: ");
            var accountNumber = lastInput;
            validateBankAccountExists(lastInput, bankAccounts);
            serviceManager.getBankAccountService().deleteBankAccount(accountNumber);
            MessageHandler.printMessage("Bank account deactivated successfully.");
        } catch (Exception e) {
            ExceptionHandler.handleException(e);
        }
    }

    private void validateBankAccountExists(String accountNumber, List<BankAccountDto> bankAccounts) {
        boolean accountExists = bankAccounts.stream()
                .anyMatch(account -> account.getAccountNumber().equals(accountNumber));

        if (!accountExists) {
            throw new IllegalArgumentException("Bank account number " + accountNumber + " not found!");
        }
    }
}
