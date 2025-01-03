package org.mandl.entities;

import jakarta.persistence.*;
import org.mandl.identity.IdentityUser;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Entity
@Table
public class BankAccount implements BaseEntity {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, unique = true)
    private String accountNumber;

    @Column(nullable = false)
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
    }

    public static void validateBalance(BigDecimal balance) {
        if (balance == null) {
            throw new IllegalArgumentException("Balance cannot be null");
        }

        if (balance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Balance must be greater than zero");
        }
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
}
