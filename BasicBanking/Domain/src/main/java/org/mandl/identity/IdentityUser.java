package org.mandl.identity;

import jakarta.persistence.*;
import org.mandl.converters.LocalDateTimeEncryptionConverter;
import org.mandl.converters.StringEncryptionConverter;
import org.mandl.entities.BankAccount;
import org.mandl.entities.BaseEntity;
import org.mandl.entities.SoftDelete;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table
public class IdentityUser implements BaseEntity, SoftDelete {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, unique = true)
    @Convert(converter = StringEncryptionConverter.class)
    private String username;

    @Column(nullable = false)
    @Convert(converter = StringEncryptionConverter.class)
    private String password;

    @Column(nullable = false)
    @Convert(converter = StringEncryptionConverter.class)
    private String salt;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.REMOVE, orphanRemoval = false)
    private List<BankAccount> accounts;

    @Column(nullable = false)
    private boolean isDeleted = false;

    @Column
    @Convert(converter = LocalDateTimeEncryptionConverter.class)
    private LocalDateTime deletedAt;

    protected IdentityUser() {

    }

    public IdentityUser(String username, String password, String salt) {
        validateUsername(username);
        this.username = username;
        this.password = password;
        this.salt = salt;
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

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    @Override
    public void markAsDeleted() {
        this.isDeleted = true;
        this.deletedAt = LocalDateTime.now();
    }

    @Override
    public boolean isDeleted() {
        return isDeleted;
    }

    @Override
    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    @Override
    public LocalDateTime getDeletedAt() {
        return this.deletedAt;
    }
}
