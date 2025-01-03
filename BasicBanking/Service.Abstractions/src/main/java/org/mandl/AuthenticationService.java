package org.mandl;

public interface AuthenticationService {
    UserDto registerUser(String username, String password) throws Exception;
    UserDto loginUser(String username, String password) throws Exception;
    void logoutUser();
}
