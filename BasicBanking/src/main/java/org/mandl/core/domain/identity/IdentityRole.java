package org.mandl.core.domain.identity;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table
public final class IdentityRole {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;

    public IdentityRole() {
    }

    private IdentityRole(UUID id, String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        this.id = id;
        this.name = name.trim().toUpperCase();
    }

    public static IdentityRole create(String name) {
        return new IdentityRole(UUID.randomUUID(), name);
    }

    public static IdentityRole create(UUID id, String name) {
        return new IdentityRole(id, name);
    }

    /// Getters & Setters

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        this.name = name.trim().toUpperCase();
    }
}
