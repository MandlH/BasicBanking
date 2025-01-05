package org.mandl.entities;

import jakarta.persistence.*;
import org.mandl.converters.BigDecimalEncryptionConverter;
import org.mandl.converters.LocalDateTimeEncryptionConverter;
import org.mandl.converters.StringEncryptionConverter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table
public class Transaction implements BaseEntity {

    @Id
    @GeneratedValue
    private UUID id;

    // Converter Conflicts with Enumerated would need other approach.
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType transactionType;

    @Column(nullable = false)
    @Convert(converter = BigDecimalEncryptionConverter.class)
    private BigDecimal amount;

    @Column(nullable = false)
    @Convert(converter = LocalDateTimeEncryptionConverter.class)
    private LocalDateTime transactionDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private BankAccount bankAccountFrom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private BankAccount bankAccountTo;

    @Column(length = 100)
    @Convert(converter = StringEncryptionConverter.class)
    private String description;

    public BankAccount getBankAccountFrom() {
        return bankAccountFrom;
    }

    public void setBankAccountFrom(BankAccount bankAccountFrom) {
        this.bankAccountFrom = bankAccountFrom;
    }

    public BankAccount getBankAccountTo() {
        return bankAccountTo;
    }

    public void setBankAccountTo(BankAccount bankAccountTo) {
        this.bankAccountTo = bankAccountTo;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        if (amount == null) {
            throw new IllegalArgumentException("Amount cannot be null");
        }

        if (amount.scale() > 2) {
            throw new IllegalArgumentException("Amount can only have up to two decimal places");
        }

        this.amount = amount;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
