package org.mandl.core.domain.identity;

import org.mandl.core.domain.entities.base.BaseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class IdentityUser extends BaseEntity {

    private String username;
    private String password;
    private List<IdentityRole> roles;
    private List<IdentityClaim> claims;

    private IdentityUser(
            UUID id,
            String username,
            String password) {
        setId(id);
        setUsername(username);
        setPassword(password);
        setRoles(new ArrayList<>());
        setClaims(new ArrayList<>());
    }

    public static IdentityUser create(String username, String password) {
        return new IdentityUser(UUID.randomUUID(), username, password);
    }

    public static IdentityUser create(UUID id, String username, String password) {
        return new IdentityUser(id, username, password);
    }

    public void assignRole(IdentityRole role) {
        if (!roles.contains(role)) {
            roles.add(role);
        }
    }

    public void assignClaim(IdentityClaim claim) {
        if (!claims.contains(claim)) {
            claims.add(claim);
        }
    }

    public boolean hasRole(String roleName) {
        return roles.stream().anyMatch(role -> role.getName().equalsIgnoreCase(roleName));
    }

    public boolean hasClaim(String claimType, String claimValue) {
        return claims.stream().anyMatch(claim ->
                claim.getType().equalsIgnoreCase(claimType) && claim.getValue().equalsIgnoreCase(claimValue)
        );
    }

    /// GETTER & SETTER

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<IdentityRole> getRoles() {
        return roles;
    }

    public void setRoles(List<IdentityRole> roles) {
        this.roles = roles;
    }

    public List<IdentityClaim> getClaims() {
        return claims;
    }

    public void setClaims(List<IdentityClaim> claims) {
        this.claims = claims;
    }
}
