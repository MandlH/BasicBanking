package org.mandl;

import java.util.List;
import java.util.UUID;

public interface IdentityUserService {
    public boolean isAuthorized(
            UUID id,
            List<RoleDto> roles,
            List<ClaimDto> claims);
    boolean isAuthenticated(UUID id);
    void registerUser(UserDto user, String password);
    UserDto getUser(UUID id);
    UserDto getUser(String username);
}
