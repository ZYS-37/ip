import java.util.Scanner;

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

    public void printDivider() {
        System.out.println(DIVIDER);
    }

    public void close() {
        scanner.close();
    }
}
