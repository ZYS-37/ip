package xerxes.ui;

import xerxes.task.Task;

import java.util.Scanner;
import java.util.List;

public class Ui {
    private static final String DIVIDER =
            "____________________________________________________________";
    public static final String BANNER = "__  __                        \n"
            + "\\ \\/ /  ___   _ __ __  __  ___  ___ \n"
            + " \\  /  / _ \\| '__|\\ \\/ / / _ \\/ __|\n"
            + " /  \\ |  __/| |    >  < |  __/\\__ \\\n"
            + "/_/\\_\\ \\___||_|   /_/\\_\\ \\___||___/";

    private final Scanner scanner;

    public Ui() {
        scanner = new Scanner(System.in);
    }

    public String readCommand() {
        return scanner.nextLine();
    }

    public void showInitialMessage() {
        printDivider();
        System.out.println(BANNER);
        showMessage("Yo wassup! I'm Xerxes.\nWhat do ya need?");
    }

    public void showMessage(String message) {
        System.out.println(message);
        printDivider();
    }

    public void showTopMessage(String message) {
        printDivider();
        System.out.println(message);
    }

    public void showTopError(String message) {
        showTopMessage(message);
    }

    public void showError(String message) {
        showMessage(message);
    }

    /**
     * Displays matching tasks with result numbering, or a no-results message.
     *
     * @param matchingTasks tasks matching the user's search keyword.
     */
    public void showMatchingTasks(List<Task> matchingTasks) {
        if (matchingTasks.isEmpty()) {
            showMessage("No matching tasks found.");
            return;
        }

        StringBuilder message = new StringBuilder("Here are the matching tasks in your list:\n");
        for (int i = 0; i < matchingTasks.size(); i++) {
            message.append(i + 1)
                    .append(". ")
                    .append(matchingTasks.get(i));
            if (i < matchingTasks.size() - 1) {
                message.append("\n");
            }
        }

        showMessage(message.toString());
    }

    public void printDivider() {
        System.out.println(DIVIDER);
    }

    public void close() {
        scanner.close();
    }
}
