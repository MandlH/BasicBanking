package org.mandl.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Default;
import jakarta.inject.Inject;
import org.mandl.BankAccountService;
import org.mandl.IdentityUserService;
import org.mandl.ServiceManager;
import org.mandl.TransactionService;

@ApplicationScoped
@Default
public class ServiceDomainManager implements ServiceManager {
    private final IdentityUserService userService;
    private final BankAccountService bankAccountService;
    private final TransactionService transactionService;

    @Inject
    public ServiceDomainManager(
            IdentityUserService userService,
            BankAccountService bankAccountService,
            TransactionService transactionService) {
        this.userService = userService;
        this.bankAccountService = bankAccountService;
        this.transactionService = transactionService;
    }

    @Override
    public IdentityUserService getIdentityUserService() {
        return userService;
    }

    @Override
    public BankAccountService getBankAccountService() {
        return bankAccountService;
    }

    @Override
    public TransactionService getTransactionService() {
        return transactionService;
    }
}
