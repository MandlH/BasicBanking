package org.mandl.controller;

import org.mandl.*;
import org.mandl.exceptions.ExceptionHandler;
import org.mandl.message.MessageHandler;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

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
            printPrompt("Enter Bank Account No.: ");
            var bankAccountNumber = getLastInput();
            var bankAccount = getServiceManager()
                    .getBankAccountService()
                    .getBankAccount(getUser().getId(), bankAccountNumber);

            if (bankAccount == null) {
                MessageHandler.printError("Bank Account Not Found");
                return;
            }

            var transactions = getServiceManager()
                    .getTransactionService()
                    .getTransactions(bankAccount.getId());

            List<String> message = Stream.concat(
                            Stream.of(String.format(
                                    "| %-15s | %-12s | %-20s | %-20s | %12s |",
                                    "Date", "Type", "From", "To", "Amount"
                            )),
                            transactions.stream()
                                    .sorted((t1, t2) -> t2.getTransactionDate().compareTo(t1.getTransactionDate()))
                                    .map(TransactionDto::toString)
                    )
                    .toList();

            MessageHandler.printMessages(message);

        } catch (Exception e) {
            ExceptionHandler.handleException(e);
        }
    }

    private void createDepositTransaction() {

    }

    private void createWithdrawTransaction() {

    }

    private void createTransferTransaction() {

    }
}
