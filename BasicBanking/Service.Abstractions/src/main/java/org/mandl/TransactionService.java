package org.mandl;

import java.util.List;
import java.util.UUID;

public interface TransactionService {
    List<TransactionDto> getTransactions(UUID bankAccountId);
    void createTransaction(TransactionDto transactionDto, String bankAccountNumberFrom, String bankAccountNumberTo);
}
