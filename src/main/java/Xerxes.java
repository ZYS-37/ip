import java.util.Objects;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

public class Xerxes {
    public static final String DIVIDER = "____________________________________________________________";

    public static final String BANNER = "__  __                        \n"
            + "\\ \\/ /  ___   _ __ __  __  ___  ___ \n"
            + " \\  /  / _ \\| '__|\\ \\/ / / _ \\/ __|\n"
            + " /  \\ |  __/| |    >  < |  __/\\__ \\\n"
            + "/_/\\_\\ \\___||_|   /_/\\_\\ \\___||___/";
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Task> tasks = new ArrayList<>();
        initialMsg();
        boolean active = true;
        while (active) {
            String userMsg = scanner.nextLine();
            if (Objects.equals(userMsg, "bye")) {
                active = false;
            } else if (Objects.equals(userMsg, "list")) {
                printTaskList(tasks);
            } else if (userMsg.matches("mark \\d+")) {
                handleTaskStatus(tasks, userMsg, true);
            } else if (userMsg.matches("unmark \\d+")) {
                handleTaskStatus(tasks, userMsg, false);
            } else {
                tasks.add(new Task(userMsg));
                msgWithDivider(userMsg);
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

    public static void printTaskList(List<Task> tasks) {
        int i = 1;
        for (Task task : tasks) {
            System.out.println((i) + ": " + task);
            i++;
        }
        printDivider();
    }

    public static void handleTaskStatus(List<Task> tasks, String input, boolean isCompleted) {
        String msgNum = input.split(" ")[1];
        int taskIndex = Integer.parseInt(msgNum) - 1;
        Task task = tasks.get(taskIndex);
        if (isCompleted) {
            task.markCompleted();
            msgWithDivider("Yippy! "+ (taskIndex + 1) + ": " + task + " has been mark completed.");
        } else {
            task.markUncompleted();
            msgWithDivider("Aww! "+ (taskIndex + 1) + ": " + task + " has been marked uncompleted.");
        }

    }
}
