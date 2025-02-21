package org.mandl.database;

import jakarta.enterprise.context.ApplicationScoped;
import org.mandl.repositories.UserContext;

import java.util.UUID;

@ApplicationScoped
public class Context implements UserContext {
    private UUID userId;
    private String username;

    public void initialize(UUID userId, String username) {
        this.userId = userId;
        this.username = username;
    }

    public UUID getUserId() {
        if (userId == null) {
            throw new IllegalStateException("UserContext has not been initialized.");
        }
        return userId;
    }

    public void reset() {
        this.userId = null;
    }

    public String getUsername() {
        return username;
    }
}
