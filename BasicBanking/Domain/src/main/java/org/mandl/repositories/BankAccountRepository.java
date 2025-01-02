package org.mandl.repositories;

import org.mandl.entities.BankAccount;

import java.util.List;
import java.util.UUID;

public interface BankAccountRepository extends BaseRepository<BankAccount> {
    List<BankAccount> getAllBankAccountsByOwnerId(UUID userId);
    BankAccount createBankAccount(BankAccount bankAccount);
    void deleteBankAccount(String accountNumber);
    BankAccount findByAccountNumber(String accountNumber);
}
