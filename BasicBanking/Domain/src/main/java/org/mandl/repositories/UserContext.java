package org.mandl.repositories;

import java.util.UUID;

public interface UserContext {
    void initialize(UUID userId, String username);
    UUID getUserId();
    String getUsername();
    void reset();
}
