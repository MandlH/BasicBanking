package org.mandl;

import java.time.LocalDateTime;
import java.util.UUID;

public class TransactionDto {
    private UUID id;
    private TransactionTypeDto transactionType;
    private double amount;
    private LocalDateTime transactionDate;
    private BankAccountDto bankAccountFrom;
    private BankAccountDto bankAccountTo;
    private String description;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public TransactionTypeDto getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionTypeDto transactionType) {
        this.transactionType = transactionType;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }

    public BankAccountDto getBankAccountFrom() {
        return bankAccountFrom;
    }

    public void setBankAccountFrom(BankAccountDto bankAccountFrom) {
        this.bankAccountFrom = bankAccountFrom;
    }

    public BankAccountDto getBankAccountTo() {
        return bankAccountTo;
    }

    public void setBankAccountTo(BankAccountDto bankAccountTo) {
        this.bankAccountTo = bankAccountTo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return String.format(
                "| %-15s | %-12s | %-20s | %-20s | %12.2f |",
                transactionDate, transactionType,
                bankAccountFrom != null ? bankAccountFrom.getAccountNumber() : "N/A",
                bankAccountTo != null ? bankAccountTo.getAccountNumber() : "N/A",
                amount
        );
    }
}
