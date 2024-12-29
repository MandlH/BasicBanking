package org.mandl.core.domain.repositories;

import org.mandl.core.domain.identity.IdentityUser;

import java.util.UUID;

public interface UserRepository {
    IdentityUser findById(UUID id);
    IdentityUser findByUsername(String username);
    void save(IdentityUser identityUser);
    void delete(IdentityUser identityUser);
    void update(IdentityUser identityUser);
}
