package org.mandl;

public enum TransactionTypeDto {
    DEPOSIT,
    WITHDRAWAL,
    TRANSFER,;

    @Override
    public String toString() {
        return name().substring(0, 1).toUpperCase() + name().substring(1).toLowerCase();
    }
}
