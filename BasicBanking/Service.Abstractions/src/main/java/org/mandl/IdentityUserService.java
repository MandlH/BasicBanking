package org.mandl;

import java.util.List;
import java.util.UUID;

public interface IdentityUserService {
    boolean isAuthorized(UUID id, List<RoleDto> roles);
    boolean isAuthenticated(UUID id);
    void resetPassword(UUID id, String password);
    UserDto registerUser(String username, String password) throws Exception;
    UserDto loginUser(String username, String password) throws Exception;
    void delete(UUID id);
    UserDto getUser(UUID id);
    UserDto getUser(String username);
}
