package org.mandl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
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

