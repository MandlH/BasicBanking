package org.mandl.repositories;

import org.mandl.identity.IdentityRole;

public interface RoleRepository {
    IdentityRole findByName(String roleName);
    void save(IdentityRole role);
    void delete(IdentityRole role);
}
