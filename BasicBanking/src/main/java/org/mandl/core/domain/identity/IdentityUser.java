package org.mandl.core.domain.identity;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "identity_users") // Table name in the database
public class IdentityUser {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    public IdentityUser() {
        // Default constructor for Hibernate
    }

    public IdentityUser(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public static IdentityUser create(String username, String password) {
        return new IdentityUser(username, password);
    }

    // Getters and setters
    public UUID getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return "IdentityUser{" +
                "id=" + id +
                ", username='" + username + '\'' +
                '}';
    }
}
