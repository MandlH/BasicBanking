package org.mandl.controller;

import org.mandl.*;
import org.mandl.exceptions.ExceptionHandler;
import org.mandl.identity.IdentityUser;
import org.mandl.message.MessageHandler;

import java.util.List;

public class BankAccountController extends BaseController {

    final String LIST_BANK_ACCOUNTS = "1";
    final String CREATE_BANK_ACCOUNT = "2";
    final String DELETE_BANK_ACCOUNT = "3";

    private BankAccountController(UserDto user, ServiceManager serviceManager) {
        super(user, serviceManager);
    }

    public static Controller create(UserDto user, ServiceManager serviceManager) {
        return new BankAccountController(user, serviceManager);
    }

    @Override
    protected void execute() {
        switch (getLastInput().toLowerCase()) {
            case LIST_BANK_ACCOUNTS:
                listBankAccounts();
                break;
            case CREATE_BANK_ACCOUNT:
                createBankAccount();
            case DELETE_BANK_ACCOUNT:
                deleteBankAccount();
                break;
        }
    }

    @Override
    protected void displayActions() {
        flushConsole();
        System.out.println("\n=================================");
        System.out.println("|    Bank Account Management    |");
        System.out.println("=================================");
        System.out.println("| 1: List Bank Accounts         |");
        System.out.println("| 2: Create Bank Account        |");
        System.out.println("| 3: Delete Bank Account        |");
        System.out.println("| back: Back                    |");
        System.out.println("| exit: Exits Application.      |");
        System.out.println("=================================");
    }

    private void listBankAccounts() {
        try {
            List<BankAccountDto> bankAccounts = getServiceManager()
                    .getBankAccountService()
                    .getAllBankAccountsByOwnerId(getUser().getId());
            System.out.println(bankAccounts);
        } catch (Exception e) {
            ExceptionHandler.handleException(e);
        }
    }

    private void createBankAccount() {
        try {
            System.out.println("\n============================");
            System.out.println("|    Create Bank Account    |");
            System.out.println("=============================");
            displayPrompt("Enter Account Number: ");
            String accountNumber = getLastInput();
            displayPrompt("Enter Balance: ");
            String balanceInput = getLastInput();
            balanceInput = balanceInput.replace(",", ".");
            double balance = Double.parseDouble(balanceInput);
            BankAccountDto bankAccountDto = new BankAccountDto(accountNumber, balance, BankAccountType.BUSINESS, getUser());
            getServiceManager().getBankAccountService().createBankAccount(bankAccountDto);
        } catch (NumberFormatException e) {
            MessageHandler.displayMessage("Invalid balance format.");
        } catch (Exception e) {
            ExceptionHandler.handleException(e);
        }

    }

    private void deleteBankAccount() {

    }
}
