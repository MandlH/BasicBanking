package org.mandl;

import org.mandl.controller.AuthenticationController;
import org.mandl.message.MessageHandler;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

public abstract class BaseController implements Controller {
    private final Scanner scanner = new Scanner(System.in);
    private final UserDto user;
    private String lastInput;
    private final String PROMPT = "> ";
    private final List<RoleDto> allowedRoles;
    private final ServiceManager serviceManager;
    private final LoggingHandler logger = LoggingHandler.getLogger(BaseController.class);

    protected BaseController(UserDto user, ServiceManager serviceManager) {
        this(user, serviceManager, null);
    }

    protected BaseController(UserDto user, ServiceManager serviceManager, List<RoleDto> allowedRoles) {
        this.user = user;
        this.serviceManager = serviceManager;
        this.allowedRoles = allowedRoles;
    }

    public void flushConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public void printPrompt(String prefix) {
        System.out.print(PROMPT + prefix);
        lastInput = scanner.nextLine().trim();
    }

    public void printPrompt() {
        System.out.print(PROMPT);
        lastInput = scanner.nextLine().trim();
    }

    public void start() {
        if (!(this instanceof AuthenticationController) && !isAuthenticatedAndAuthorized(getAllowedRoles())) {
            logger.warn(this.user.getUsername() + " attempted to enter " + this.getClass().getSimpleName());
            Controller authenticationController = ControllerFactory.getAuthenticationController(serviceManager);
            authenticationController.start();
            return;
        }

        while (true) {
            flushConsole();
            displayActions();
            printPrompt();

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

    protected abstract Map<String, String> getOptions();

    protected abstract String getMenuTitle();

    protected void displayActions() {
        flushConsole();
        MessageHandler.printMenu(getMenuTitle(), getOptions());
    }

    public String getLastInput() {
        return lastInput;
    }

    public UserDto getUser() {
        return user;
    }

    public List<RoleDto> getAllowedRoles() {
        return allowedRoles;
    }

    public ServiceManager getServiceManager() {
        return serviceManager;
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
}
