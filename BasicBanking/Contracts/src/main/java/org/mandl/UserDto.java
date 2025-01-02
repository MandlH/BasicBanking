package org.mandl;

import java.util.List;
import java.util.UUID;

public class UserDto {
    private UUID id;
    private String username;
    private List<BankAccountDto> bankAccounts;

    public UserDto() {
    }

    public UserDto(
            UUID id,
            String username
    ) {
        this.username = username;
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UUID getId() {
        return id;
    }

    @Override
    public String toString() {
        return String.format(
                """
                        User Information:
                          %-15s: %s
                          %-15s: %s""",
                "Username", username,
                "Bank Accounts", bankAccounts != null
                        ? bankAccounts.stream()
                        .map(BankAccountDto::getAccountNumber)
                        .toList()
                        : "None"
        );
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public List<BankAccountDto> getBankAccounts() {
        return bankAccounts;
    }

    public void setBankAccounts(List<BankAccountDto> bankAccounts) {
        this.bankAccounts = bankAccounts;
    }
}
