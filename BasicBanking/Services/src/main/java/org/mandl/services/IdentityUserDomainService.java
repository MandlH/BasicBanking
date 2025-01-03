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

    private final IdentityUserRepository identityUserRepository;
    private final RepositoryWrapper repositoryWrapper;

    @Inject
    public IdentityUserDomainService(RepositoryWrapper repositoryWrapper) {
        this.repositoryWrapper = repositoryWrapper;
        this.identityUserRepository = repositoryWrapper.getIdentityUserRepository();
    }

    @Override
    public boolean isAuthorized(UUID id, List<RoleDto> roles) {
        var user = identityUserRepository.findById(id);
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
        return identityUserRepository.findById(id) != null;
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
            repositoryWrapper.rollbackTransaction();
            throw new ServiceException("Password could not be retested", e);
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
            System.err.println("Deletion failed: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            repositoryWrapper.rollbackTransaction();
            throw new RuntimeException("Failed to delete user with ID " + id, e);
        }
    }

    @Override
    public UserDto getUser(UUID id) {
        var user = identityUserRepository.findById(id);
        if (user == null) {
            throw new IllegalArgumentException("User could not be found.");
        }

        return UserMapper.INSTANCE.domainToDto(user);
    }

    @Override
    public UserDto getUser(String username) {
        var user = identityUserRepository.getLoginUser(username);
        if (user == null) {
            throw new IllegalArgumentException("User could not be found.");
        }
        return UserMapper.INSTANCE.domainToDto(user);
    }
}
