package org.mandl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.hibernate.service.spi.ServiceException;
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
    private static final LoggingHandler logger = LoggingHandler.getLogger(BankAccountDomainService.class);
    private final IdentityUserRepository identityUserRepository;
    private final RepositoryWrapper repositoryWrapper;

    @Inject
    public IdentityUserDomainService(RepositoryWrapper repositoryWrapper) {
        this.repositoryWrapper = repositoryWrapper;
        this.identityUserRepository = repositoryWrapper.getIdentityUserRepository();
    }

    @Override
    public boolean isAuthorized(UUID id, List<RoleDto> roles) {
        try {
            var user = identityUserRepository.findById(id);
            if (user == null) {
                throw new IllegalArgumentException("User not found.");
            }
            List<IdentityRole> identityRoles = roles
                    .stream()
                    .map(RoleMapper.INSTANCE::dtoToDomain)
                    .toList();

            return user.isAuthorized(identityRoles);
        } catch (SecurityException e) {
            logger.error("Error during authorization", e);
            return false;
        }
    }

    @Override
    public boolean isAuthenticated(UUID id) {
        try {
            return identityUserRepository.findById(id) != null;
        } catch (SecurityException e) {
            logger.error("Error during authorization", e);
            return false;
        }
    }

    @Override
    public void resetPassword(UUID id, String password) {
        try {
            var user = identityUserRepository.findById(id);
            if (user == null) {
                throw new IllegalArgumentException("User not found.");
            }
            user.setPassword(hashPassword(password)); // Use secure hashing
            identityUserRepository.update(user);
        } catch (Exception e) {
            logger.error("Unexpected error while resetting password for user ID: " + id, e);
            throw new ServiceException("An unexpected error occurred while resetting the password.", e);
        }
    }


    @Override
    public UserDto registerUser(String username, String password) throws AuthenticationException {
        try {
            if (identityUserRepository.findByUsername(username) != null) {
                throw new AuthenticationException("Username already exists!");
            }

            var newUser = new IdentityUser(username, hashPassword(password));
            identityUserRepository.save(newUser);

            var savedUser = identityUserRepository.findByUsername(username);
            if (savedUser == null) {
                logger.error("User registration failed: Unable to retrieve saved user with username " + username);
                throw new ServiceException("An unexpected error occurred: User registration verification failed.");
            }

            return UserMapper.INSTANCE.domainToDto(savedUser);
        } catch (AuthenticationException e) {
            throw e;

        } catch (Exception e) {
            logger.error("An unexpected error occurred while registering user " + username, e);
            throw new ServiceException("An unexpected error occurred while registering. Please try again.", e);
        }
    }


    @Override
    public UserDto loginUser(String username, String password) throws AuthenticationException {
        try {
            var identityUser = identityUserRepository.findByUsername(username);
            if (identityUser == null || !verifyPassword(password, identityUser.getPassword())) {
                throw new AuthenticationException("Username or Password are wrong!");
            }
            return UserMapper.INSTANCE.domainToDto(identityUser);
        } catch (AuthenticationException e) {
            throw e;
        } catch (Exception e) {
            logger.error("An unexpected error occurred while logining user " + username, e);
            return null;
        }
    }

    @Override
    public void delete(UUID id) {
        try {
            var user = identityUserRepository.findById(id);
            if (user == null) {
                throw new IllegalArgumentException("User with ID " + id + " not found.");
            }

            identityUserRepository.delete(user);
        } catch (IllegalArgumentException e) {
            logger.warn(e.getMessage(), e);
            System.err.println("Deletion failed: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("An unexpected error occurred while deleting user with ID " + id + ": " + e.getMessage());
            throw new RuntimeException("Failed to delete user with ID " + id, e);
        }
    }


    @Override
    public UserDto getUser(UUID id) {
        var user = identityUserRepository.findById(id);
        if (user == null) {
            throw new IllegalArgumentException("User not found.");
        }

        return UserMapper.INSTANCE.domainToDto(user);
    }

    @Override
    public UserDto getUser(String username) {
        var user = identityUserRepository.findByUsername(username);
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
