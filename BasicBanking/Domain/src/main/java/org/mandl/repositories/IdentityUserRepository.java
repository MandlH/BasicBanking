package org.mandl.repositories;

import org.mandl.identity.IdentityUser;

import java.util.UUID;

public interface IdentityUserRepository {
    IdentityUser findById(UUID id);
    IdentityUser findByUsername(String username);
    void save(IdentityUser identityUser);
    void delete(IdentityUser identityUser);
    void update(IdentityUser identityUser);
}
