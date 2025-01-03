package org.mandl.identity;

import jakarta.persistence.*;
import org.mandl.entities.BankAccount;
import org.mandl.entities.BaseEntity;

import java.util.List;
import java.util.UUID;

@Entity
@Table
public class IdentityUser implements BaseEntity {

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

    protected IdentityUser() {

    }

    public IdentityUser(String username, String password) {
        validateUsername(username);
        validatePassword(password);
        this.username = username;
        this.password = password;
    }

    public boolean isAuthorized(List<IdentityRole> requiredRoles) {
        if (requiredRoles == null || requiredRoles.isEmpty()) {
            return true;
        }
        return roles != null && roles.stream().anyMatch(role -> requiredRoles.contains(role.getName()));
    }

    public static void validatePassword(String password) {
        // Password is not always required therefore it can be null after authentication!
        if (password == null) {
            return;
        }

        String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";

        if (!password.matches(passwordRegex)) {
            throw new IllegalArgumentException(
                    "Password must have at least 8 characters, including one uppercase letter, one lowercase letter, one number, and one special character."
            );
        }
    }

    public static void validateUsername(String username) {
        if (username == null) {
            throw new IllegalArgumentException("Username cannot be null.");
        }

        String usernameRegex = "^[a-zA-Z0-9]{3,20}$";

        if (!username.matches(usernameRegex)) {
            throw new IllegalArgumentException(
                    "Invalid username! Username must be 3-20 characters long and alphanumeric only."
            );
        }
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
        validateUsername(username);
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        validatePassword(password);
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
}
