package org.mandl.repositories;

import org.mandl.entities.Transaction;
import org.mandl.entities.TransactionType;

import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends BaseRepository<Transaction>  {
    List<Transaction> getUserTransactions(TransactionType type, UUID bankAccountId);
    List<Transaction> getUserTransactions(UUID bankAccountId);
}
