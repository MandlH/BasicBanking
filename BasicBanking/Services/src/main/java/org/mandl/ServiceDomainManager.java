package org.mandl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Default;
import jakarta.inject.Inject;

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
