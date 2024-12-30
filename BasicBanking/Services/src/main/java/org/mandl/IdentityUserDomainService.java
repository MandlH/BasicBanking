package org.mandl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.mandl.identity.IdentityClaim;
import org.mandl.identity.IdentityRole;
import org.mandl.identity.IdentityUser;
import org.mandl.mapper.ClaimMapper;
import org.mandl.mapper.RoleMapper;
import org.mandl.repositories.IdentityUserRepository;
import org.mandl.repositories.RepositoryWrapper;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
final class IdentityUserDomainService implements IdentityUserService {
    private final IdentityUserRepository repository;

    @Inject
    public IdentityUserDomainService(RepositoryWrapper repositoryWrapper) {
        this.repository = repositoryWrapper.getIdentityUserRepository();
    }

    @Override
    public boolean isAuthorized(
            UUID id,
            List<RoleDto> roles,
            List<ClaimDto> claims) {
        IdentityUser user = repository.findById(id);

        List<IdentityRole> identityRoles = roles
                .stream()
                .map(RoleMapper.INSTANCE::roleDtoToIdentityRole)
                .toList();
        List<IdentityClaim> identityClaims = claims
                .stream()
                .map(ClaimMapper.INSTANCE::claimDtoToIdentityClaim)
                .toList();

        return user.isAuthorized(identityRoles, identityClaims);
    }

    @Override
    public boolean isAuthenticated(UUID id) {
        return false;
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
        UserDto userDto = new UserDto();
        return userDto;
    }
}
