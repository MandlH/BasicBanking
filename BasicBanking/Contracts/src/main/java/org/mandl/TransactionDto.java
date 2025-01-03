package org.mandl;

import java.text.SimpleDateFormat;
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

    public TransactionDto(
            TransactionTypeDto transactionType,
            double amount,
            LocalDateTime transactionDate,
            BankAccountDto bankAccountFrom,
            BankAccountDto bankAccountTo){
        this.transactionType = transactionType;
        this.amount = amount;
        this.transactionDate = transactionDate;
        this.bankAccountFrom = bankAccountFrom;
        this.bankAccountTo = bankAccountTo;
    }

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
        var dateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm");
        String formattedDate = transactionDate != null ? dateFormat.format(transactionDate) : "N/A";

        return String.format(
                "| %-23s | %-12s | %-20s | %-20s | %12.2f |",
                formattedDate, transactionType,
                bankAccountFrom != null ? bankAccountFrom.getAccountNumber() : "N/A",
                bankAccountTo != null ? bankAccountTo.getAccountNumber() : "N/A",
                amount
        );
    }
}
