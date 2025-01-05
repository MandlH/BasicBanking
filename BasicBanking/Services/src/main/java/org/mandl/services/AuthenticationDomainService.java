package org.mandl.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.mandl.AuthenticationService;
import org.mandl.UserDto;
import org.mandl.exceptions.AuthenticationException;
import org.mandl.identity.IdentityUser;
import org.mandl.mapper.UserMapper;
import org.mandl.repositories.IdentityUserRepository;
import org.mandl.repositories.RepositoryWrapper;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.UUID;

@ApplicationScoped
public class AuthenticationDomainService
        extends BaseDomainService
        implements AuthenticationService {

    private final IdentityUserRepository identityUserRepository;
    private final RepositoryWrapper repositoryWrapper;

    @Inject
    public AuthenticationDomainService(RepositoryWrapper repositoryWrapper) {
        this.repositoryWrapper = repositoryWrapper;
        this.identityUserRepository = repositoryWrapper.getIdentityUserRepository();
    }

    @Override
    public UserDto registerUser(String username, String password) throws Exception {
        try {
            repositoryWrapper.beginTransaction();

            if (identityUserRepository.getLoginUser(username) != null) {
                throw new AuthenticationException("User with username already exists!: " + username);
            }

            var salt = PasswordService.generateSalt();
            var hashedPassword = PasswordService.hashPassword(password, salt);

            var newUser = new IdentityUser(username, hashedPassword, salt);
            identityUserRepository.save(newUser);
            repositoryWrapper.commitTransaction();

            var savedUser = identityUserRepository.getLoginUser(username);
            if (savedUser == null) {
                throw new NullPointerException("User with username " + username + " should exist but could not be found");
            }

            initializeUserContext(savedUser.getId(), savedUser.getUsername());
            logger.info("Registered user: " + username);
            return UserMapper.INSTANCE.domainToDto(savedUser);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | NullPointerException e) {
            repositoryWrapper.rollbackTransaction();
            throw new AuthenticationException("An internal error occurred during registration. Please try again later.", e);
        } catch (AuthenticationException | IllegalArgumentException e) {
            repositoryWrapper.rollbackTransaction();
            throw e;
        } catch (Exception e) {
            repositoryWrapper.rollbackTransaction();
            throw new AuthenticationException("Error occurred during registration.", e);
        }
    }

    @Override
    public UserDto loginUser(String username, String password) throws AuthenticationException {
        try {
            logger.info("Attempting to login user: " + username);
            var identityUser = identityUserRepository.getLoginUser(username);

            if (identityUser == null || identityUser.isDeleted()) {
                throw new AuthenticationException("Invalid username or password.");
            }

            var isPasswordVerified = PasswordService.verifyPassword(password, identityUser.getSalt(), identityUser.getPassword());
            if (!isPasswordVerified) {
                throw new AuthenticationException("Invalid username or password.");
            }

            initializeUserContext(identityUser.getId(), identityUser.getUsername());

            var user = UserMapper.INSTANCE.domainToDto(identityUser);
            if (user == null) {
                throw new NullPointerException("Logged in user should not be null");
            }

            logger.info("Logged in user: " + username);
            return user;
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | IllegalStateException | NullPointerException e) {
            throw new AuthenticationException("An error occurred during authentication.", e);
        } catch (AuthenticationException e) {
            throw e;
        } catch (Exception e) {
            throw new AuthenticationException("Error occurred during login.", e);
        }
    }

    @Override
    public void logoutUser() {
        userContext.reset();
        logger.info("Logged out user");
    }

    private void initializeUserContext(UUID userId, String username) {
        try {
            userContext.initialize(userId, username);
            logger.info("Context successfully set for " + username);
        } catch (Exception e) {
            userContext.reset();
            throw new IllegalStateException("Failed to initialize user context.", e);
        }
    }
}
