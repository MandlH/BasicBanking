package org.mandl.entities;

import jakarta.persistence.*;
import org.mandl.identity.IdentityUser;

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
    private double balance;

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

    public BankAccount(String accountNumber, double balance, BankAccountType accountType, IdentityUser owner) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.accountType = accountType;
        this.owner = owner;
    }

    // Default constructor required by JPA
    protected BankAccount() {
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

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
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
