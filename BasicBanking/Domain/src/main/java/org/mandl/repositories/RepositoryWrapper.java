package org.mandl.repositories;

import java.util.UUID;

public interface RepositoryWrapper {
    void beginTransaction();
    void commitTransaction();
    void rollbackTransaction();
    IdentityUserRepository getIdentityUserRepository();
    BankAccountRepository getBankAccountRepository();
    TransactionRepository getTransactionRepository();
}
