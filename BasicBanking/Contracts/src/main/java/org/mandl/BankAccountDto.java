package org.mandl;

import java.util.UUID;

public class BankAccountDto {
    private UUID id;
    private String accountNumber;
    private double balance;
    private BankAccountType accountType;
    private UserDto owner;

    public BankAccountDto(String accountNumber, double balance, BankAccountType accountType, UserDto owner) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.accountType = accountType;
        this.owner = owner;
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

    public UserDto getOwner() {
        return owner;
    }

    public void setOwner(UserDto owner) {
        this.owner = owner;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
