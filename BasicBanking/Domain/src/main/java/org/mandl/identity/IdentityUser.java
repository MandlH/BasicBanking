package org.mandl.identity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table
public class IdentityUser {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "id")
    private List<IdentityRole> roles = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "id")
    private List<IdentityClaim> claims = new ArrayList<>();


    public IdentityUser() {
    }

    public IdentityUser(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public static IdentityUser create(String username, String password) {
        return new IdentityUser(username, password);
    }

    public UUID getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public boolean isAuthorized(List<IdentityRole> requiredRoles, List<IdentityClaim> requiredClaims) {
        boolean hasRequiredRole = requiredRoles == null || requiredRoles.isEmpty() ||
                (roles != null && roles.stream().anyMatch(role -> requiredRoles.contains(role.getName())));

        boolean hasRequiredClaim = requiredClaims == null || requiredClaims.isEmpty() ||
                (claims != null && claims.stream().anyMatch(userClaim ->
                        requiredClaims.stream().anyMatch(requiredClaim ->
                                requiredClaim.getType().equals(userClaim.getType()) &&
                                        requiredClaim.getValue().equals(userClaim.getValue())
                        )
                ));

        return hasRequiredRole && hasRequiredClaim;
    }


    @Override
    public String toString() {
        return "IdentityUser{" +
                "id=" + id +
                ", username='" + username + '\'' +
                '}';
    }
}
