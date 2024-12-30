package org.mandl;

public class BankAccountController extends BaseController {

    final String LIST_BANK_ACCOUNTS = "1";
    final String CREATE_BANK_ACCOUNT = "2";
    final String DELETE_BANK_ACCOUNT = "3";

    private BankAccountController(UserDto user, ServiceManager serviceManager) {
        super(user, serviceManager);
    }

    public static Controller create(UserDto user, ServiceManager serviceManager) {
        return new BankAccountController(user, serviceManager);
    }

    @Override
    protected void execute() {
        switch (getLastInput().toLowerCase()) {
            case LIST_BANK_ACCOUNTS:
                listBankAccounts();
                break;
            case CREATE_BANK_ACCOUNT:
                createBankAccount();
            case DELETE_BANK_ACCOUNT:
                deleteBankAccount();
                break;
        }
    }

    @Override
    protected void displayActions() {
        flushConsole();
        System.out.println("\n================================");
        System.out.println("|    Bank Account Management    |");
        System.out.println("=================================");
        System.out.println("| 1: List Bank Accounts         |");
        System.out.println("| 2: Create Bank Account        |");
        System.out.println("| 3: Delete Bank Account        |");
        System.out.println("| back: Back                    |");
        System.out.println("| exit: Exits Application.      |");
        System.out.println("=================================");
    }

    private void listBankAccounts() {
    }

    private void createBankAccount() {

    }

    private void deleteBankAccount() {

    }
}
