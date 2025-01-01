package org.mandl.repositories;

import org.mandl.identity.IdentityUser;

public interface IdentityUserRepository extends BaseRepository<IdentityUser> {
    IdentityUser findByUsername(String username);
}
