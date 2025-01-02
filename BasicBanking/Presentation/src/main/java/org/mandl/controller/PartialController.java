package org.mandl.controller;

import org.mandl.BankAccountDto;
import org.mandl.TransactionDto;
import org.mandl.message.MessageHandler;

import java.util.List;
import java.util.stream.Stream;

public class PartialController {
    static void PrintBankAccounts(List<BankAccountDto> bankAccounts, boolean waitForUserAction) {
        var message = Stream.concat(
                        Stream.of(String.format(
                                "| %-15s | %-12s | %-10s |",
                                "Account No.", "Balance", "Type"
                        )),
                        bankAccounts.stream()
                                .map(BankAccountDto::toString)
                )
                .toList();
        if (waitForUserAction) {
            MessageHandler.printMessages(message);
        } else {
            MessageHandler.printMessagesWithoutWaitForAction(message);
        }
    }

    static void PrintTransactions(List<TransactionDto> transactions, boolean waitForUserAction) {
        List<String> message = Stream.concat(
                Stream.of(String.format(
                        "| %-23s | %-12s | %-20s | %-20s | %12s |",
                        "Date", "Type", "From", "To", "Amount"
                )),
                transactions.stream()
                        .sorted((t1, t2) -> t2.getTransactionDate().compareTo(t1.getTransactionDate()))
                        .map(TransactionDto::toString)
        ).toList();

        if (waitForUserAction) {
            MessageHandler.printMessages(message);
        } else {
            MessageHandler.printMessagesWithoutWaitForAction(message);
        }
    }
}
