package org.mandl;

import java.math.BigDecimal;
import java.util.UUID;

public class BankAccountDto {
    private UUID id;
    private String accountNumber;
    private BigDecimal balance;
    private BankAccountTypeDto accountType;
    private UserDto owner;

    public BankAccountDto(String accountNumber, BigDecimal balance, BankAccountTypeDto accountType, UserDto owner) {
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

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BankAccountTypeDto getAccountType() {
        return accountType;
    }

    public void setAccountType(BankAccountTypeDto accountType) {
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

    @Override
    public String toString() {
        return String.format(
                "| %-15s | %12.2f | %-10s |",
                accountNumber, balance, accountType
        );
    }
}
