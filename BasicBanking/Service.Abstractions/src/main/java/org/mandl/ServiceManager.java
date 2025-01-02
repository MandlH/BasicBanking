package org.mandl;

public interface ServiceManager {
    IdentityUserService getIdentityUserService();
    BankAccountService getBankAccountService();
    TransactionService getTransactionService();
}
