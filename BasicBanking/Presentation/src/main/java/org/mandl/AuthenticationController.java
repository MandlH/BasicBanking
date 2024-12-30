package org.mandl;

public class AuthenticationController extends BaseController {

    final String LOGIN = "1";
    final String REGISTER = "2";

    public AuthenticationController(ServiceManager serviceManager) {
        super(null, serviceManager);
    }

    @Override
    protected void execute() {
        switch (getLastInput().toLowerCase()) {
            case LOGIN:
                startLogin();
                break;
            case REGISTER:
                startRegister();
                break;
        }
    }

    @Override
    protected void displayActions() {
        flushConsole();
        System.out.println("\n============================");
        System.out.println("|          WELCOME         |");
        System.out.println("============================");
        System.out.println("| 1: Move Login            |");
        System.out.println("| 2: Move Register         |");
        System.out.println("| exit: Exits Application. |");
        System.out.println("============================");
    }

    private void startLogin() {
        flushConsole();
        System.out.println("\n============================");
        System.out.println("|           Login          |");
        System.out.println("============================");
        displayPrompt("Enter Username: ");
        String username = getLastInput();
        displayPrompt("Enter Password: ");
        String password = getLastInput();
        validateLogin(username, password);
    }

    private void validateLogin(String username, String password) {
        UserDto user = serviceManager.getIdentityUserService().loginUser(username, password);
        System.out.println(user.toString());
    }

    private void startRegister() {
        flushConsole();
        System.out.println("\n============================");
        System.out.println("|         Register         |");
        System.out.println("============================");
        displayPrompt("Enter Username: ");
        String username = getLastInput();
        displayPrompt("Enter Password: ");
        String password = getLastInput();
        validateRegister(username, password);
    }

    private void validateRegister(String username, String password) {
        UserDto user = serviceManager.getIdentityUserService().registerUser(username, password);
        System.out.println(user.toString());
    }
}
