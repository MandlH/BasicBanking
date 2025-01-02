package org.mandl.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Default;
import jakarta.inject.Inject;
import org.mandl.*;

@ApplicationScoped
@Default
public class ServiceDomainManager
        implements ServiceManager {

    private final IdentityUserService userService;
    private final BankAccountService bankAccountService;
    private final TransactionService transactionService;
    private final AuthenticationDomainService authenticationService;

    @Inject
    public ServiceDomainManager(
            IdentityUserService userService,
            BankAccountService bankAccountService,
            TransactionService transactionService,
            AuthenticationService authenticationService, AuthenticationDomainService authenticationDomainService) {
        this.userService = userService;
        this.bankAccountService = bankAccountService;
        this.transactionService = transactionService;
        this.authenticationService = authenticationDomainService;
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

    @Override
    public AuthenticationService getAuthenticationService() {
        return authenticationService;
    }
}
