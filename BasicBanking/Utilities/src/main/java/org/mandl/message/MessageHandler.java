package org.mandl.message;

import org.mandl.LoggingHandler;
import org.mandl.exceptions.ExceptionHandler;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class MessageHandler {
    private static final Scanner scanner = new Scanner(System.in);

    public static void printMessage(String message) {
        int length = 7 + message.length();
        System.out.println("-".repeat(length));
        System.out.println(message);
        System.out.println("-".repeat(length));
        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }

    public static void printMessages(List<String> messages) {
        printMessagesWithoutWaitForAction(messages);
        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }

    public static void printMessagesWithoutWaitForAction(List<String> messages) {
        if (messages == null || messages.isEmpty()) {
            System.out.println("No messages to display.");
            return;
        }

        int maxLength = messages.stream()
                .mapToInt(String::length)
                .max()
                .orElse(0);

        int borderLength = maxLength + 6;
        String border = "-".repeat(borderLength);

        System.out.println(border);
        for (String message : messages) {
            String padding = " ".repeat((borderLength - message.length() - 2) / 2);
            System.out.printf("%s%s%s\n", padding, message, padding);
        }
        System.out.println(border);
    }

    public static void printHeader(String title) {
        int borderLength = title.length() + 6;
        printTitle(title, borderLength);
    }

    public static void printMenu(String title, Map<String, String> options) {
        int maxLength = Math.max(
                title.length(),
                options.values().stream().mapToInt(String::length).max().orElse(0)
        );
        maxLength = Math.max(maxLength, "back: Back".length());
        maxLength = Math.max(maxLength, "exit: Exit Application".length());

        int borderLength = maxLength + 6; // 6 = 2 for "| |" and 4 for padding
        printTitle(title, borderLength);

        int index = 1;
        for (Map.Entry<String, String> entry : options.entrySet()) {
            System.out.printf("| %s: %-"+(borderLength - 5)+"s|\n", entry.getKey(), entry.getValue());
            index++;
        }

        System.out.printf("| %-"+(borderLength - 2)+"s|\n", "back: Back");
        System.out.printf("| %-"+(borderLength - 2)+"s|\n", "exit: Exit Application");
        System.out.println("=".repeat(borderLength));
    }

    private static void printTitle(String title, int borderLength) {
        String border = "=".repeat(borderLength);

        System.out.println("\n" + border);
        String repeat = " ".repeat((borderLength - title.length() - 1) / 2);
        System.out.printf("|%s%s%s|\n", repeat, title, repeat);
        System.out.println(border);
    }

    public static void printExceptionMessage(String errorMessage) {
        var borderLength = errorMessage.length() + 10;
        String border = "*".repeat(borderLength);

        System.out.println("\n" + border);
        String padding = " ".repeat((borderLength - errorMessage.length() - 2) / 2);
        System.out.printf("*%s%s%s*\n", padding, errorMessage, padding);
        System.out.println(border);
        System.out.printf("%s%s\n", "Enter to acknowledge...", padding);

        scanner.nextLine();
    }
}
