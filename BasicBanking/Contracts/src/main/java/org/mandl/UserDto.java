package org.mandl;

import java.util.List;
import java.util.UUID;

public class UserDto {
    private UUID id;
    private String username;

    public UserDto() {
    }

    public UserDto(
        String username
    ) {
        this.username = username;
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
}
