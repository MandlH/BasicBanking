package org.mandl.repositories;

import org.mandl.identity.IdentityUser;

import java.util.UUID;

public interface IdentityUserRepository extends BaseRepository<IdentityUser> {
    IdentityUser findByUsername(String username);
}
