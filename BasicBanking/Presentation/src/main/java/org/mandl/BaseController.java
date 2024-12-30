package org.mandl;

import java.util.List;
import java.util.Scanner;

public abstract class BaseController implements Controller {
    final Scanner scanner = new Scanner(System.in);
    private final UserDto user;
    private String lastInput;
    private final String PROMPT = "> ";
    private List<RoleDto> allowedRoles;
    ServiceManager serviceManager;
    LoggingHandler logger = LoggingHandler.getLogger(BaseController.class);

    protected BaseController(
            UserDto user,
            ServiceManager serviceManager) {
        this.user = user;
        this.serviceManager = serviceManager;
    }

    protected BaseController(
            UserDto user,
            ServiceManager serviceManager,
            List<RoleDto> allowedRoles) {
        this.user = user;
        this.serviceManager = serviceManager;
        this.allowedRoles = allowedRoles;
    }

    /// Only for Linux terminal
    public void flushConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public void displayPrompt(String prefix) {
        System.out.print(PROMPT + prefix);
        lastInput = scanner.nextLine().trim();
    }

    public void displayPrompt() {
        System.out.print(PROMPT);
        lastInput = scanner.nextLine().trim();
    }

    private boolean isAuthenticated() {
        return user != null;
    }

    private boolean isAuthorized(List<RoleDto> roles) {
        if (roles == null || roles.isEmpty()) {
            return true;
        }

        return serviceManager.getIdentityUserService().isAuthorized(user.getId(), roles);
    }

    private boolean isAuthenticatedAndAuthorized(List<RoleDto> roles) {
        return isAuthenticated() && isAuthorized(roles);
    }

    public void start() {
        if (!(this instanceof AuthenticationController)){
            if (!isAuthenticatedAndAuthorized(getAllowedRoles())){
                logger.warn(this.user.getUsername() + " attempted to enter " + this.getClass().getSimpleName());
                Controller authenticationController = ControllerFactory.getAuthenticationController(serviceManager);
                authenticationController.start();
                return;
            }
        }

        while (true) {
            flushConsole();
            displayActions();
            displayPrompt();

            if (lastInput.equalsIgnoreCase("back") && user != null) {
                break;
            }

            if (lastInput.equalsIgnoreCase("exit")) {
                System.exit(0);
            }

            execute();
        }
    }

    protected abstract void execute();

    protected abstract void displayActions();

    public String getLastInput() {
        return lastInput;
    }

    public UserDto getUser() {
        return user;
    }

    public List<RoleDto> getAllowedRoles() {
        return allowedRoles;
    }
}
