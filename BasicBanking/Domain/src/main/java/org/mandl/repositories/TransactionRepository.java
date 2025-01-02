package org.mandl.repositories;

import org.mandl.entities.BankAccountType;
import org.mandl.entities.Transaction;
import org.mandl.entities.TransactionType;

import java.util.List;
import java.util.UUID;

public interface TransactionRepository {
    List<Transaction> getTransactions(TransactionType type, UUID bankAccountId);
    List<Transaction> getTransactions(UUID bankAccountId);
}
