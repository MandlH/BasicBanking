package org.mandl;

import java.util.UUID;

public class UserDto {
    private UUID id;
    private String username;

    public UserDto() {
    }

    public UserDto(
            UUID id,
            String username
    ) {
        this.username = username;
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UUID getId() {
        return id;
    }

    @Override
    public String toString() {
        return "UserDto{" + "id=" + id + ", username=" + username + '}';
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
