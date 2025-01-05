package org.mandl;

import java.util.List;

public interface BankAccountService {
    List<BankAccountDto> getAllBankAccounts();
    BankAccountDto createBankAccount(BankAccountDto bankAccountDto);
    void deleteBankAccount(String accountNumber);
    BankAccountDto getBankAccount(String accountNumber);
}
