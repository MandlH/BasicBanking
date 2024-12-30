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
    public void resetPassword(UUID id, String password) {
        //TODO HASH Password
        IdentityUser user = repository.findById(id);
        user.setPassword(password);
        repository.update(user);
    }

    @Override
    public UserDto registerUser(String username, String password) throws AuthenticationException {
        IdentityUser identityUser = repository.findByUsername(username);

        if (identityUser != null) {
            throw new AuthenticationException("Username already exists!");
        }

        repository.save(new IdentityUser(username, password));
        IdentityUser newIdentityUser = repository.findByUsername(username);

        if (newIdentityUser == null) {
            throw new AuthenticationException("Automatic login failed!");
        }

        return UserMapper.INSTANCE.identityUserToUserDto(newIdentityUser);
    }

    @Override
    public UserDto loginUser(String username, String password) throws AuthenticationException {
        IdentityUser identityUser = repository.findByUsername(username);
        //TODO ADD HASHING
        if (identityUser == null) {
            throw new AuthenticationException("Username or Password are Wrong!");
        }

        if (!password.equals(identityUser.getPassword())) {
            throw new AuthenticationException("Username or Password are Wrong!");
        }

        return UserMapper.INSTANCE.identityUserToUserDto(identityUser);
    }

    @Override
    public void delete(UUID id) {
        IdentityUser user = repository.findById(id);
        repository.delete(user);
    }

    @Override
    public UserDto getUser(UUID id) {
        return UserMapper.INSTANCE.identityUserToUserDto(repository.findById(id));
    }

    @Override
    public UserDto getUser(String username) {
        IdentityUser user = repository.findByUsername(username);
        UserDto userDto = new UserDto();
        return userDto;
    }
}
