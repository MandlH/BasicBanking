package org.mandl.repositories;

import org.mandl.entities.BankAccount;

import java.util.List;

public interface BankAccountRepository extends BaseRepository<BankAccount> {
    List<BankAccount> getAllBankAccountsFromOwner();
    BankAccount createBankAccount(BankAccount bankAccount);
    BankAccount findByAccountNumber(String accountNumber);
}
