package org.mandl.core.domain.identity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

public class IdentityRole {

    @Id
    @GeneratedValue
    private UUID id;
    private final String name;

    private IdentityRole(UUID id, String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        this.name = name.trim().toUpperCase();
        this.id = id;
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
