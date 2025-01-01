package org.mandl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.mandl.exceptions.AuthenticationException;
import org.mandl.identity.IdentityRole;
import org.mandl.identity.IdentityUser;
import org.mandl.mapper.RoleMapper;
import org.mandl.mapper.UserMapper;
import org.mandl.repositories.IdentityUserRepository;
import org.mandl.repositories.RepositoryWrapper;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
final class IdentityUserDomainService implements IdentityUserService {
    private final IdentityUserRepository repository;
    private final RepositoryWrapper repositoryWrapper;

    @Inject
    public IdentityUserDomainService(RepositoryWrapper repositoryWrapper) {
        this.repositoryWrapper = repositoryWrapper;
        this.repository = repositoryWrapper.getIdentityUserRepository();
    }

    @Override
    public boolean isAuthorized(UUID id, List<RoleDto> roles) {
        IdentityUser user = repository.findById(id);
        if (user == null) {
            throw new IllegalArgumentException("User not found.");
        }
        List<IdentityRole> identityRoles = roles
                .stream()
                .map(RoleMapper.INSTANCE::dtoToDomain)
                .toList();

        return user.isAuthorized(identityRoles);
    }

    @Override
    public boolean isAuthenticated(UUID id) {
        return repository.findById(id) != null;
    }

    @Override
    public void resetPassword(UUID id, String password) {
        IdentityUser user = repository.findById(id);
        if (user == null) {
            throw new IllegalArgumentException("User not found.");
        }
        user.setPassword(hashPassword(password)); // Use secure hashing
        repository.update(user);
    }

    @Override
    public UserDto registerUser(String username, String password) throws AuthenticationException {
        if (repository.findByUsername(username) != null) {
            throw new AuthenticationException("Username already exists!");
        }

        IdentityUser newUser = new IdentityUser(username, hashPassword(password));
        repository.save(newUser);

        return UserMapper.INSTANCE.domainToDto(repository.findByUsername(username));
    }

    @Override
    public UserDto loginUser(String username, String password) throws AuthenticationException {
        IdentityUser identityUser = repository.findByUsername(username);
        if (identityUser == null || !verifyPassword(password, identityUser.getPassword())) {
            throw new AuthenticationException("Username or Password are wrong!");
        }
        return UserMapper.INSTANCE.domainToDto(identityUser);
    }

    @Override
    public void delete(UUID id) {
        IdentityUser user = repository.findById(id);
        if (user == null) {
            throw new IllegalArgumentException("User not found.");
        }
        repository.delete(user);
    }

    @Override
    public UserDto getUser(UUID id) {
        IdentityUser user = repository.findById(id);
        if (user == null) {
            throw new IllegalArgumentException("User not found.");
        }

        return UserMapper.INSTANCE.domainToDto(user);
    }

    @Override
    public UserDto getUser(String username) {
        IdentityUser user = repository.findByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("User not found.");
        }
        return UserMapper.INSTANCE.domainToDto(user);
    }

    private String hashPassword(String password) {
        // Implement password hashing
        return password; // Placeholder
    }

    private boolean verifyPassword(String password, String hashedPassword) {
        // Implement password verification
        return password.equals(hashedPassword); // Placeholder
    }
}
