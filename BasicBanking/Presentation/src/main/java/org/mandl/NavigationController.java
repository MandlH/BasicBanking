package org.mandl;

public class NavigationController extends BaseController {

    final String BANK_ACCOUNT_MANAGEMENT = "1";
    final String TRANSACTION_MANAGEMENT = "2";
    final String USER_ACCOUNT_MANAGEMENT = "3";
    final String LOGOUT = "4";

    private NavigationController(UserDto user, ServiceManager serviceManager) {
        super(user, serviceManager);
    }

    public static Controller create(UserDto user, ServiceManager serviceManager) {
        return new NavigationController(user, serviceManager);
    }

    @Override
    protected void execute() {
        switch (getLastInput().toLowerCase()) {
            case BANK_ACCOUNT_MANAGEMENT:
                break;
            case TRANSACTION_MANAGEMENT:
                break;
            case USER_ACCOUNT_MANAGEMENT:
                break;
            case LOGOUT:
                ControllerFactory.getAuthenticationController(serviceManager).start(null);
                break;
        }
    }

    @Override
    protected void displayActions() {
        flushConsole();
        System.out.println("\n================================");
        System.out.println("|           Navigation          |");
        System.out.println("=================================");
        System.out.println("| 1: Bank Accounts Management   |");
        System.out.println("| 2: Transaction Management     |");
        System.out.println("| 3: User Account Management    |");
        System.out.println("| 4: Logout                     |");
        System.out.println("| back: Back                    |");
        System.out.println("| exit: Exits Application.      |");
        System.out.println("=================================");
    }
}
