package org.mandl.controller;

import org.mandl.*;
import org.mandl.exceptions.ExceptionHandler;
import org.mandl.message.MessageHandler;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

public class TransactionController extends BaseController {

    private final String TRANSACTION_HISTORY = "Transaction History";
    private final String DEPOSIT_TRANSACTION = "Deposit Transaction";
    private final String WITHDRAW_TRANSACTION = "Withdraw Transaction";
    private final String TRANSFER_TRANSACTION = "Transfer Transaction";

    private TransactionController(UserDto user, ServiceManager serviceManager) {
        super(user, serviceManager);
    }

    public static Controller create(UserDto user, ServiceManager serviceManager) {
        return new TransactionController(user, serviceManager);
    }

    @Override
    protected Map<String, String> getOptions() {
        Map<String, String> options = new LinkedHashMap<>();
        options.put("1", TRANSACTION_HISTORY);
        options.put("2", DEPOSIT_TRANSACTION);
        options.put("3", WITHDRAW_TRANSACTION);
        options.put("4", TRANSFER_TRANSACTION);
        return options;
    }

    @Override
    protected String getMenuTitle() {
        return "Transaction Management";
    }

    @Override
    protected void execute() {
        String action = getOptions().get(getLastInput());
        if (action == null) return;

        switch (action) {
            case TRANSACTION_HISTORY -> listTransactionHistory();
            case DEPOSIT_TRANSACTION -> createDepositTransaction();
            case WITHDRAW_TRANSACTION -> createWithdrawTransaction();
            case TRANSFER_TRANSACTION -> createTransferTransaction();
        }
    }

    private void listTransactionHistory() {
        try {
            MessageHandler.printHeader(TRANSACTION_HISTORY);
            var bankAccountService = getServiceManager().getBankAccountService();

            var bankAccounts = bankAccountService.getAllBankAccountsByOwnerId(getUser().getId());
            PartialController.PrintBankAccounts(bankAccounts, false);

            printPrompt("Enter Bank Account No.: ");
            var bankAccountNumber = getLastInput();

            var bankAccount = bankAccountService.getBankAccount(getUser().getId(), bankAccountNumber);
            var transactions = getServiceManager()
                    .getTransactionService()
                    .getTransactions(bankAccount.getId());
            PartialController.PrintTransactions(transactions, true);
        } catch (Exception e) {
            ExceptionHandler.handleException(e);
        }
    }

    private void createDepositTransaction() {
        try {
            MessageHandler.printHeader(DEPOSIT_TRANSACTION);
            var bankAccountService = getServiceManager().getBankAccountService();

            var bankAccounts = bankAccountService.getAllBankAccountsByOwnerId(getUser().getId());
            PartialController.PrintBankAccounts(bankAccounts, false);
            printPrompt("Enter Bank Account No.: ");
            var bankAccountNumber = getLastInput();

            var bankAccount = bankAccountService.getBankAccount(getUser().getId(), bankAccountNumber);

            double amount = getValidatedAmount("Enter Amount to Deposit: ", true);

            var depositTransaction = new TransactionDto(
                    TransactionTypeDto.DEPOSIT,
                    amount,
                    LocalDateTime.now(),
                    null,
                    bankAccount
            );

            printPrompt("Enter Description (Optional): ");
            String description = getLastInput();

            if (!description.isBlank()){
                depositTransaction.setDescription(description);
            }

            getServiceManager().getTransactionService().createTransaction(depositTransaction, null, bankAccount);
            MessageHandler.printMessage("Deposit Transaction created successfully.");
        } catch (Exception e) {
            ExceptionHandler.handleException(e);
        }
    }

    private void createWithdrawTransaction() {
        try {
            MessageHandler.printHeader(WITHDRAW_TRANSACTION);
            var bankAccountService = getServiceManager().getBankAccountService();

            var bankAccounts = bankAccountService.getAllBankAccountsByOwnerId(getUser().getId());
            PartialController.PrintBankAccounts(bankAccounts, false);
            printPrompt("Enter Bank Account No.: ");
            var bankAccountNumber = getLastInput();

            var bankAccount = bankAccountService.getBankAccount(getUser().getId(), bankAccountNumber);

            double amount = getValidatedAmount("Enter Amount to Withdraw: ", false);

            var withdrawTransaction = new TransactionDto(
                    TransactionTypeDto.WITHDRAWAL,
                    amount,
                    LocalDateTime.now(),
                    bankAccount,
                    null
            );

            printPrompt("Enter Description (Optional): ");
            String description = getLastInput();

            if (!description.isBlank()) {
                withdrawTransaction.setDescription(description);
            }

            getServiceManager().getTransactionService().createTransaction(withdrawTransaction, bankAccount, null);
            MessageHandler.printMessage("Withdraw Transaction created successfully.");
        } catch (Exception e) {
            ExceptionHandler.handleException(e);
        }
    }

    private void createTransferTransaction() {
        try {
            MessageHandler.printHeader(TRANSFER_TRANSACTION);
            var bankAccountService = getServiceManager().getBankAccountService();

            var bankAccounts = bankAccountService.getAllBankAccountsByOwnerId(getUser().getId());
            PartialController.PrintBankAccounts(bankAccounts, false);
            printPrompt("Enter Source Bank Account No.: ");
            var sourceAccountNumber = getLastInput();

            var sourceAccount = bankAccountService.getBankAccount(getUser().getId(), sourceAccountNumber);

            printPrompt("Enter Destination Bank Account No.: ");
            var destinationAccountNumber = getLastInput();

            var destinationAccount = bankAccountService.getBankAccount(null, destinationAccountNumber);

            double amount = getValidatedAmount("Enter Amount to Transfer: ", true);

            var transferTransaction = new TransactionDto(
                    TransactionTypeDto.TRANSFER,
                    amount,
                    LocalDateTime.now(),
                    sourceAccount,
                    destinationAccount
            );

            printPrompt("Enter Description (Optional): ");
            String description = getLastInput();

            if (!description.isBlank()) {
                transferTransaction.setDescription(description);
            }

            getServiceManager().getTransactionService().createTransaction(transferTransaction, sourceAccount, destinationAccount);
            MessageHandler.printMessage("Transfer Transaction created successfully.");
        } catch (Exception e) {
            ExceptionHandler.handleException(e);
        }
    }

    private double getValidatedAmount(String promptMessage, boolean isPositive) {
        while (true) {
            printPrompt(promptMessage);
            try {
                String input = getLastInput().replace(",", ".");
                double amount = Double.parseDouble(input);

                if ((isPositive && amount <= 0) || (!isPositive && amount >= 0)) {
                    throw new IllegalArgumentException("Amount must be " + (isPositive ? "positive." : "negative."));
                }
                return amount;
            } catch (NumberFormatException e) {
                MessageHandler.printError("Invalid input. Please enter a valid number.");
            } catch (IllegalArgumentException e) {
                MessageHandler.printError(e.getMessage());
            }
        }
    }
}
