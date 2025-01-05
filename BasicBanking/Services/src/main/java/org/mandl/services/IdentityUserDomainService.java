package org.mandl.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.hibernate.service.spi.ServiceException;
import org.mandl.IdentityUserService;
import org.mandl.repositories.IdentityUserRepository;
import org.mandl.repositories.RepositoryWrapper;

@ApplicationScoped
final class IdentityUserDomainService
    extends BaseDomainService
        implements IdentityUserService {

    private final IdentityUserRepository identityUserRepository;
    private final RepositoryWrapper repositoryWrapper;

    @Inject
    public IdentityUserDomainService(RepositoryWrapper repositoryWrapper) {
        this.repositoryWrapper = repositoryWrapper;
        this.identityUserRepository = repositoryWrapper.getIdentityUserRepository();
    }

    @Override
    public void resetPassword(String password) {
        try {
            repositoryWrapper.beginTransaction();
            var id = userContext.getUserId();
            var user = identityUserRepository.findById(id);
            var salt = PasswordService.generateSalt();
            var hashedPassword = PasswordService.hashPassword(password, salt);
            user.setPassword(hashedPassword);
            user.setSalt(salt);
            identityUserRepository.update(user);
            repositoryWrapper.commitTransaction();
            logger.info("Password reset successful by " + userContext.getUsername());
        } catch (Exception e) {
            repositoryWrapper.rollbackTransaction();
            throw new ServiceException("Password could not be changed", e);
        }
    }

    @Override
    public void deactivateUser() {
        try {
            repositoryWrapper.beginTransaction();
            var id = userContext.getUserId();
            var user = identityUserRepository.findById(id);
            identityUserRepository.delete(user);
            repositoryWrapper.commitTransaction();
            logger.info("User deactivated by " + userContext.getUsername());
        } catch (Exception e) {
            repositoryWrapper.rollbackTransaction();
            throw new ServiceException("Failed to deactivate user " + userContext.getUsername(), e);
        }
    }
}
