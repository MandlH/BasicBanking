package org.mandl;

import java.util.UUID;

public interface IdentityUserService {
    public void registerUser(UserDto user, String password);
    public UserDto getUser(UUID id);
    public UserDto getUser(String username);
}
