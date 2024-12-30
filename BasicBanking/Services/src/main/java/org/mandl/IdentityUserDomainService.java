package org.mandl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
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

    @Inject
    public IdentityUserDomainService(RepositoryWrapper repositoryWrapper) {
        this.repository = repositoryWrapper.getIdentityUserRepository();
    }

    @Override
    public boolean isAuthorized(
            UUID id,
            List<RoleDto> roles) {
        IdentityUser user = repository.findById(id);

        List<IdentityRole> identityRoles = roles
                .stream()
                .map(RoleMapper.INSTANCE::roleDtoToIdentityRole)
                .toList();

        return user.isAuthorized(identityRoles);
    }

    @Override
    public boolean isAuthenticated(UUID id) {
        return false;
    }

    @Override
    public UserDto registerUser(String username, String password) {
        IdentityUser identityUser = repository.findByUsername(username);

        if (identityUser != null) {
            return null;
        }

        repository.save(new IdentityUser(username, password));
        IdentityUser newIdentityUser = repository.findByUsername(username);

        if (newIdentityUser == null) {
            return null;
        }

        return UserMapper.INSTANCE.identityUserToUserDto(newIdentityUser);
    }

    @Override
    public UserDto loginUser(String username, String password) {
        IdentityUser identityUser = repository.findByUsername(username);
        //TODO ADD HASHING
        if (identityUser == null) {
            return null;
        }

        if (!password.equals(identityUser.getPassword())) {
            return null;
        }

        return UserMapper.INSTANCE.identityUserToUserDto(identityUser);
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
