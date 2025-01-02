package org.mandl.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Default;
import jakarta.inject.Inject;
import org.mandl.BankAccountService;
import org.mandl.IdentityUserService;
import org.mandl.ServiceManager;

@ApplicationScoped
@Default
public class ServiceDomainManager implements ServiceManager {
    private final IdentityUserService userService;
    private final BankAccountService bankAccountService;

    @Inject
    public ServiceDomainManager(
            IdentityUserService userService,
            BankAccountService bankAccountService) {
        this.userService = userService;
        this.bankAccountService = bankAccountService;
    }

    @Override
    public IdentityUserService getIdentityUserService() {
        return userService;
    }

    @Override
    public BankAccountService getBankAccountService() {
        return bankAccountService;
    }
}
