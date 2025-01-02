package org.mandl;

import java.util.List;
import java.util.UUID;

public interface TransactionService {
    List<TransactionDto> getTransactions(TransactionTypeDto type, UUID bankAccountId);
    List<TransactionDto> getTransactions(UUID bankAccountId);
    void createTransaction(TransactionDto transaction, BankAccountDto bankAccountFrom, BankAccountDto bankAccountTo);
}
