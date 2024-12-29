package org.mandl.core.domain.repositories;

import org.mandl.core.domain.identity.IdentityRole;

public interface RoleRepository {
    IdentityRole findByName(String roleName);
    void save(IdentityRole role);
    void delete(IdentityRole role);
}
