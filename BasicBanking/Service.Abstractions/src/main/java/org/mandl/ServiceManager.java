package org.mandl;

public interface ServiceManager {
    IdentityUserService getIdentityUserService();
    BankAccountService getBankAccountService();
}
