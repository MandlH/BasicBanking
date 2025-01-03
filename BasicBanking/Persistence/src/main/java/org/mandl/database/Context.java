package org.mandl.database;

import jakarta.enterprise.context.ApplicationScoped;
import org.mandl.repositories.UserContext;

import java.util.UUID;

@ApplicationScoped
public class Context implements UserContext {
    private UUID userId;

    public void initialize(UUID userId) {
        if (this.userId != null) {
            throw new IllegalStateException("UserContext is already initialized.");
        }
        this.userId = userId;
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
}
