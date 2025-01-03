package org.mandl.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.hibernate.service.spi.ServiceException;
import org.mandl.AuthenticationService;
import org.mandl.LoggingHandler;
import org.mandl.UserDto;
import org.mandl.exceptions.AuthenticationException;
import org.mandl.identity.IdentityUser;
import org.mandl.mapper.UserMapper;
import org.mandl.repositories.IdentityUserRepository;
import org.mandl.repositories.RepositoryWrapper;
import org.mandl.repositories.UserContext;

import java.util.UUID;

@ApplicationScoped
public class AuthenticationDomainService
        implements AuthenticationService {

    private static final LoggingHandler logger = LoggingHandler.getLogger(BankAccountDomainService.class);
    private final IdentityUserRepository identityUserRepository;
    private final RepositoryWrapper repositoryWrapper;
    private final UserContext userContext;

    @Inject
    public AuthenticationDomainService(RepositoryWrapper repositoryWrapper, UserContext userContext) {
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

            var newUser = new IdentityUser(username, password);
            identityUserRepository.save(newUser);
            repositoryWrapper.commitTransaction();

            var savedUser = identityUserRepository.getLoginUser(username);
            if (savedUser == null) {
                throw new ServiceException("User registration verification failed.");
            }

            initializeUserContext(savedUser.getId());
            return UserMapper.INSTANCE.domainToDto(savedUser);
        } catch (AuthenticationException | IllegalArgumentException | ServiceException e) {
            repositoryWrapper.rollbackTransaction();
            throw e;
        }
    }

    @Override
    public UserDto loginUser(String username, String password) throws AuthenticationException {
        var identityUser = identityUserRepository.getLoginUser(username);
        if (identityUser == null || !verifyPassword(password, identityUser.getPassword())) {
            throw new AuthenticationException("Username or Password are wrong!");
        }

        initializeUserContext(identityUser.getId());

        var user = UserMapper.INSTANCE.domainToDto(identityUser);

        if (user == null) {
            throw new ServiceException("User login verification failed.");
        }

        return user;
    }

    @Override
    public void logoutUser() {
        userContext.reset();
    }

    private void initializeUserContext(UUID userId) {
        try {
            userContext.initialize(userId);
        } catch (IllegalStateException e) {
            logger.error("UserContext initialization failed: " + e.getMessage(), e);
            userContext.reset();
            throw new ServiceException("Failed to initialize user context.", e);
        }
    }

    private boolean verifyPassword(String password, String hashedPassword) {
        return password.equals(hashedPassword);
    }
}
