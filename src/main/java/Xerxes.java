import java.util.Objects;
import java.util.Scanner;

public class Xerxes {
    public static final String DIVIDER = "____________________________________________________________";

    public static final String BANNER = " __  __                        \n"
            + "\\ \\/ /  ___   _ __ __  __  ___  ___ \n"
            + " \\  /  / _ \\| '__|\\ \\/ / / _ \\/ __|\n"
            + " /  \\ |  __/| |    >  < |  __/\\__ \\\n"
            + "/_/\\_\\ \\___||_|   /_/\\_\\ \\___||___/";
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        initialMsg();
        boolean echo = true;
        while (echo) {
            String userMsg = scanner.nextLine();
            if (!Objects.equals(userMsg, "bye")) {
                msgWithDivider(userMsg);
            } else {
                echo = false;
            }
        }
        msgWithDivider("Ciao, cya again");
    }

    public static void printDivider() {
        System.out.println(DIVIDER);
    }
    public static void msgWithDivider(String msg) {
        System.out.println(msg);
        printDivider();
    }

    public static void initialMsg() {
        printDivider();
        System.out.println(BANNER);
        msgWithDivider("Yo wassup! I'm Xerxes.\nWhat do ya need?");
    }
}
