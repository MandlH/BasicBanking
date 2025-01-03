package org.mandl.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.hibernate.service.spi.ServiceException;
import org.mandl.IdentityUserService;
import org.mandl.LoggingHandler;
import org.mandl.RoleDto;
import org.mandl.UserDto;
import org.mandl.identity.IdentityRole;
import org.mandl.mapper.RoleMapper;
import org.mandl.mapper.UserMapper;
import org.mandl.repositories.IdentityUserRepository;
import org.mandl.repositories.RepositoryWrapper;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
final class IdentityUserDomainService
        implements IdentityUserService {

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
            repositoryWrapper.beginTransaction();
            var user = identityUserRepository.findById(id);
            if (user == null) {
                throw new IllegalArgumentException("User not found.");
            }
            user.setPassword(password);
            identityUserRepository.update(user);
            repositoryWrapper.commitTransaction();
        } catch (Exception e) {
            logger.error("Unexpected error while resetting password for user ID: " + id, e);
            throw new ServiceException("An unexpected error occurred while resetting the password.", e);
        }
    }

    @Override
    public void delete(UUID id) {
        try {
            repositoryWrapper.beginTransaction();
            var user = identityUserRepository.findById(id);
            if (user == null) {
                throw new IllegalArgumentException("User with ID " + id + " not found.");
            }

            identityUserRepository.delete(user);
            repositoryWrapper.commitTransaction();
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
        var user = identityUserRepository.getLoginUser(username);
        if (user == null) {
            throw new IllegalArgumentException("User not found.");
        }
        return UserMapper.INSTANCE.domainToDto(user);
    }
}
