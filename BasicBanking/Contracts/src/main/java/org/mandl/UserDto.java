package org.mandl;

public class UserDto {
    private String username;

    public UserDto(String firstName) {
        this.username = firstName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
