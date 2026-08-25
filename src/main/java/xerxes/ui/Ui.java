package xerxes.ui;

import java.util.Scanner;

/**
 * Handles all console input and output for the Xerxes application.
 */
public class Ui {
    /** Divider printed after most user-facing messages. */
    private static final String DIVIDER =
            "____________________________________________________________";

    /** ASCII-art banner displayed when the application starts. */
    public static final String BANNER = "__  __                        \n"
            + "\\ \\/ /  ___   _ __ __  __  ___  ___ \n"
            + " \\  /  / _ \\| '__|\\ \\/ / / _ \\/ __|\n"
            + " /  \\ |  __/| |    >  < |  __/\\__ \\\n"
            + "/_/\\_\\ \\___||_|   /_/\\_\\ \\___||___/";

    /** Reads commands from the standard input stream. */
    private final Scanner scanner;

    /** Creates a user interface that reads from standard input. */
    public Ui() {
        scanner = new Scanner(System.in);
    }

    /**
     * Reads one command entered by the user.
     *
     * @return The command line entered by the user.
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /** Displays the welcome banner and introductory message. */
    public void showInitialMessage() {
        printDivider();
        System.out.println(BANNER);
        showMessage("Yo wassup! I'm Xerxes.\nWhat do ya need?");
    }

    /**
     * Displays a normal message followed by a divider.
     *
     * @param message Message to display.
     */
    public void showMessage(String message) {
        System.out.println(message);
        printDivider();
    }

    /**
     * Displays a message with a divider above it, for startup messages.
     *
     * @param message Message to display.
     */
    public void showTopMessage(String message) {
        printDivider();
        System.out.println(message);
    }

    /**
     * Displays an error that occurs before the main interaction loop starts.
     *
     * @param message Error message to display.
     */
    public void showTopError(String message) {
        showTopMessage(message);
    }

    /**
     * Displays an error message followed by a divider.
     *
     * @param message Error message to display.
     */
    public void showError(String message) {
        showMessage(message);
    }

    /** Prints a visual divider in the console. */
    public void printDivider() {
        System.out.println(DIVIDER);
    }

    /** Closes the scanner used to read console input. */
    public void close() {
        scanner.close();
    }
}
