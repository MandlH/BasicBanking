package org.mandl;

import org.mandl.controller.AuthenticationController;
import org.mandl.message.MessageHandler;

import java.util.Map;
import java.util.Scanner;

public abstract class BaseController implements Controller {
    private final Scanner scanner = new Scanner(System.in);
    protected final ServiceManager serviceManager;
    protected final UserDto user;
    protected String lastInput;
    private final String PROMPT = "> ";

    protected BaseController(UserDto user, ServiceManager serviceManager) {
        this.user = user;
        this.serviceManager = serviceManager;
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
        while (true) {
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
        MessageHandler.printMenu(getMenuTitle(), getOptions());
    }
}
