package org.mandl.core.domain.identity;

import org.mandl.core.domain.entities.base.BaseEntity;

import java.util.UUID;

public class IdentityRole extends BaseEntity {
    private final String name;

    private IdentityRole(UUID id, String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        this.name = name.trim().toUpperCase();
        setId(id);
    }

    public static IdentityRole create(String name) {
        return new IdentityRole(UUID.randomUUID(), name);
    }

    public static IdentityRole create(UUID id, String name) {
        return new IdentityRole(id, name);
    }

    public String getName() {
        return name;
    }
}
