package org.mandl;

import java.util.List;
import java.util.UUID;

public interface BankAccountService {
    List<BankAccountDto> getAllBankAccountsByOwnerId(UUID ownerId);
    BankAccountDto createBankAccount(BankAccountDto bankAccountDto);
    void deleteBankAccount(String accountNumber);
}
