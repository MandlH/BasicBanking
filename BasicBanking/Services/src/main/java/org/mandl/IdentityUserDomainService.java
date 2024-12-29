package org.mandl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.mandl.identity.IdentityUser;
import org.mandl.repositories.IdentityUserRepository;
import org.mandl.repositories.RepositoryWrapper;

import java.util.UUID;

@ApplicationScoped
final class IdentityUserDomainService implements IdentityUserService {
    private final IdentityUserRepository repository;

    @Inject
    public IdentityUserDomainService(RepositoryWrapper repositoryWrapper) {
        this.repository = repositoryWrapper.getIdentityUserRepository();
    }

    @Override
    public void registerUser(UserDto user, String password) {
        repository.save(new IdentityUser(user.getUsername(), password));
    }

    @Override
    public UserDto getUser(UUID id) {
        return null;
    }

    @Override
    public UserDto getUser(String username) {
        IdentityUser user = repository.findByUsername(username);
        UserDto userDto = new UserDto(user.getUsername());
        return userDto;
    }
}
