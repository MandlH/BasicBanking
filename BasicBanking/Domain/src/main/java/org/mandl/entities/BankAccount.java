package org.mandl.entities;

import jakarta.persistence.*;
import org.mandl.converters.BigDecimalEncryptionConverter;
import org.mandl.converters.LocalDateTimeEncryptionConverter;
import org.mandl.converters.StringEncryptionConverter;
import org.mandl.identity.IdentityUser;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table
public class BankAccount implements BaseEntity, SoftDelete {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, unique = true)
    @Convert(converter = StringEncryptionConverter.class)
    private String accountNumber;

    @Column(nullable = false)
    @Convert(converter = BigDecimalEncryptionConverter.class)
    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BankAccountType accountType;

    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.PERSIST)
    @JoinColumn(nullable = false)
    private IdentityUser owner;

    @OneToMany(mappedBy = "bankAccountFrom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> transactionsFrom;

    @OneToMany(mappedBy = "bankAccountTo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> transactionsTo;

    @Column(nullable = false)
    private boolean isDeleted = false;

    @Column
    @Convert(converter = LocalDateTimeEncryptionConverter.class)
    private LocalDateTime deletedAt;

    protected BankAccount() {

    }

    public BankAccount(String accountNumber, BigDecimal balance, BankAccountType accountType, IdentityUser owner) {
        validateAccountNumber(accountNumber);
        validateBalance(balance);
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.accountType = accountType;
        this.owner = owner;
    }

    public static void validateAccountNumber(String accountNumber) {
        if (accountNumber == null) {
            throw new IllegalArgumentException("Account number cannot be null");
        }

        if (accountNumber.length() != 10) {
            throw new IllegalArgumentException("Account number length must be 10");
        }

        String prefix = accountNumber.substring(0, 2);
        if (!prefix.matches("[A-Z]{2}")) {
            throw new IllegalArgumentException("The first two characters of the account number must be uppercase letters");
        }
    }

    public static void validateBalance(BigDecimal balance) {
        if (balance == null) {
            throw new IllegalArgumentException("Balance cannot be null");
        }

        if (balance.scale() > 2) {
            throw new IllegalArgumentException("Amount can only have up to two decimal places");
        }

        if (balance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Balance must be greater than zero");
        }
    }

    public String getMaskedAccountNumber() {
        var firstFourChars = this.accountNumber.substring(0, 4);
        var maskedPart = "x".repeat(this.accountNumber.length() - 4);
        return firstFourChars + maskedPart;
    }

    public UUID getId() {
        return id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void deposit(BigDecimal amount) {
        this.setBalance(this.getBalance().add(amount));
    }

    public void withdraw(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) > 0) {
            throw new IllegalArgumentException("Amount have to be negative!");
        }

        var newBalance = this.getBalance().add(amount);
        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Insufficient funds.");
        }

        this.setBalance(newBalance);
    }

    public void setBalance(BigDecimal balance) {
        validateBalance(balance);
        this.balance = balance;
    }

    public BankAccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(BankAccountType accountType) {
        this.accountType = accountType;
    }

    public IdentityUser getOwner() {
        return owner;
    }

    public void setOwner(IdentityUser owner) {
        this.owner = owner;
    }

    public List<Transaction> getTransactionsFrom() {
        return transactionsFrom;
    }

    public void setTransactionsFrom(List<Transaction> transactionsFrom) {
        this.transactionsFrom = transactionsFrom;
    }

    public List<Transaction> getTransactionsTo() {
        return transactionsTo;
    }

    public void setTransactionsTo(List<Transaction> transactionsTo) {
        this.transactionsTo = transactionsTo;
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
