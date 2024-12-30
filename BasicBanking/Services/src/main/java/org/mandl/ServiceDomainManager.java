package org.mandl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Default;
import jakarta.inject.Inject;

@ApplicationScoped
@Default
public class ServiceDomainManager implements ServiceManager {
    private final IdentityUserService userService;

    @Inject
    public ServiceDomainManager(IdentityUserService userService) {
        this.userService = userService;
    }

    @Override
    public IdentityUserService getIdentityUserService() {
        return userService;
    }
}

