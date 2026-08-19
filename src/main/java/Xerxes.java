public class Xerxes {
    public static final String DIVIDER = "____________________________________________________________";

    public static final String BANNER = " __  __                        \n"
            + "\\ \\/ /  ___   _ __ __  __  ___  ___ \n"
            + " \\  /  / _ \\| '__|\\ \\/ / / _ \\/ __|\n"
            + " /  \\ |  __/| |    >  < |  __/\\__ \\\n"
            + "/_/\\_\\ \\___||_|   /_/\\_\\ \\___||___/";
    public static void main(String[] args) {

        initialMsg();
        msgWithDivider("Ciao, cya again");
    }

    public static void printDivider() {
        System.out.println(DIVIDER);
    }
    public static (String msg) {
        System.out.println(msg);
        printDivider();
    }

    public static void initialMsg() {
        printDivider();
        System.out.println(BANNER);
        msgWithDivider("Yo wassup! I'm Xerxes.\nWhat do ya need?");
    }
}
