package org.mandl.controller;

import org.mandl.*;

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

    }
    
    private void createDepositTransaction() {

    }

    private void createWithdrawTransaction() {

    }

    private void createTransferTransaction() {

    }
}
