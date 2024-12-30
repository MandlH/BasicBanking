package org.mandl;

import java.util.List;
import java.util.UUID;

public interface IdentityUserService {
    public boolean isAuthorized(
            UUID id,
            List<RoleDto> roles);
    boolean isAuthenticated(UUID id);
    UserDto registerUser(String username, String password) throws Exception;
    UserDto loginUser(String username, String password) throws Exception;
    UserDto getUser(UUID id);
    UserDto getUser(String username);
}
