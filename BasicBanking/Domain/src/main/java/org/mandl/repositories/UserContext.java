package org.mandl.repositories;

import java.util.UUID;

public interface UserContext {
    void initialize(UUID userId);
    UUID getUserId();
    void reset();
}
