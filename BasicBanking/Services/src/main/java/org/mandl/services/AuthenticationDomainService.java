package org.mandl.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.hibernate.service.spi.ServiceException;
import org.mandl.AuthenticationService;
import org.mandl.UserDto;
import org.mandl.exceptions.AuthenticationException;
import org.mandl.identity.IdentityUser;
import org.mandl.mapper.UserMapper;
import org.mandl.repositories.IdentityUserRepository;
import org.mandl.repositories.RepositoryWrapper;
import org.mandl.repositories.UserContext;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.UUID;

@ApplicationScoped
public class AuthenticationDomainService
        implements AuthenticationService {

    private final IdentityUserRepository identityUserRepository;
    private final RepositoryWrapper repositoryWrapper;
    private final UserContext userContext;

    @Inject
    public AuthenticationDomainService(
            RepositoryWrapper repositoryWrapper,
            UserContext userContext) {
        this.repositoryWrapper = repositoryWrapper;
        this.userContext = userContext;
        this.identityUserRepository = repositoryWrapper.getIdentityUserRepository();
    }

    @Override
    public UserDto registerUser(String username, String password) throws AuthenticationException {
        try {
            repositoryWrapper.beginTransaction();
            if (identityUserRepository.getLoginUser(username) != null) {
                throw new AuthenticationException("Username already exists!");
            }

            var salt = PasswordService.generateSalt();
            var hashedPassword = PasswordService.hashPassword(password, salt);

            var newUser = new IdentityUser(username, hashedPassword, salt);
            identityUserRepository.save(newUser);
            repositoryWrapper.commitTransaction();

            var savedUser = identityUserRepository.getLoginUser(username);
            if (savedUser == null) {
                throw new ServiceException("User registration verification failed.");
            }

            initializeUserContext(savedUser.getId());
            return UserMapper.INSTANCE.domainToDto(savedUser);
        }catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new AuthenticationException("Registration Failed!");
        }  catch (AuthenticationException | IllegalArgumentException | ServiceException e) {
            repositoryWrapper.rollbackTransaction();
            throw e;
        }
    }

    @Override
    public UserDto loginUser(String username, String password) throws AuthenticationException {
        try {
            var identityUser = identityUserRepository.getLoginUser(username);

            if (identityUser == null) {
                throw new AuthenticationException("Username or Password are wrong!");
            }

            var isPasswordVerified = PasswordService.verifyPassword(password, identityUser.getSalt(), identityUser.getPassword());

            if (!isPasswordVerified) {
                throw new AuthenticationException("Username or Password are wrong!");
            }

            initializeUserContext(identityUser.getId());

            var user = UserMapper.INSTANCE.domainToDto(identityUser);

            if (user == null) {
                throw new ServiceException("User login verification failed.");
            }

            return user;
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new AuthenticationException("Username or Password are wrong!");
        }
    }

    @Override
    public void logoutUser() {
        userContext.reset();
    }

    private void initializeUserContext(UUID userId) {
        try {
            userContext.initialize(userId);
        } catch (Exception e) {
            userContext.reset();
            throw new IllegalStateException("Failed to initialize user context.", e);
        }
    }
}
