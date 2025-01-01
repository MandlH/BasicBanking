package org.mandl.identity;

import jakarta.persistence.*;
import org.mandl.entities.BankAccount;

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

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BankAccount> accounts;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_id")
    private List<IdentityRole> roles;

    public IdentityUser() {
        this.accounts = new ArrayList<>();
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

    public void setId(UUID id) {
        this.id = id;
    }

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

    public List<BankAccount> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<BankAccount> accounts) {
        this.accounts = accounts;
    }

    public void addAccount(BankAccount account) {
        accounts.add(account);
        account.setOwner(this);
    }

    public void removeAccount(BankAccount account) {
        accounts.remove(account);
        account.setOwner(null);
    }

    public List<IdentityRole> getRoles() {
        return roles;
    }

    public void setRoles(List<IdentityRole> roles) {
        this.roles = roles;
    }

    public boolean isAuthorized(List<IdentityRole> requiredRoles) {
        if (requiredRoles == null || requiredRoles.isEmpty()) {
            return true;
        }
        return roles != null && roles.stream().anyMatch(role -> requiredRoles.contains(role.getName()));
    }
}
