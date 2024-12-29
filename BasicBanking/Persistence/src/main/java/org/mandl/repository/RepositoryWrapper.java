package org.mandl.repository;

import jakarta.annotation.Nonnull;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.mandl.repositories.IdentityUserRepository;

@ApplicationScoped
@Nonnull
public class RepositoryWrapper implements org.mandl.repositories.RepositoryWrapper {

    private final IdentityUserRepository userRepository;

    @Inject
    public RepositoryWrapper(IdentityUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public IdentityUserRepository getIdentityUserRepository() {
        return userRepository;
    }
}
