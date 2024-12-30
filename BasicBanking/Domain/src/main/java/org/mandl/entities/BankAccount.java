package org.mandl.entities;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table
public class BankAccount {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String accountNumber;

    @Column(nullable = false)
    private double balance;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BankAccountType accountType;
}
