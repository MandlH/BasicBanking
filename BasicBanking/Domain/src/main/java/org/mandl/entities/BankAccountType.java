package org.mandl.entities;

public enum BankAccountType {
    SAVING,
    CHECKING,
    BUSINESS;

    @Override
    public String toString() {
        return name().substring(0, 1).toUpperCase() + name().substring(1).toLowerCase();
    }
}

