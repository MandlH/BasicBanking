package org.mandl.repositories;

public interface RepositoryWrapper {
    void beginTransaction();
    void commitTransaction();
    void rollbackTransaction();
    IdentityUserRepository getIdentityUserRepository();
    BankAccountRepository getBankAccountRepository();
    TransactionRepository getTransactionRepository();
}
