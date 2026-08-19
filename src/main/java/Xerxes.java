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
            } else if (userMsg.startsWith("todo ")) {
                handleAddToDo(tasks,userMsg);
            } else if (userMsg.startsWith("deadline ")) {
                handleAddDeadline(tasks, userMsg);
            } else if (userMsg.startsWith("event ")) {
                handleAddEvent(tasks, userMsg);
            } else {
                msgWithDivider("I dont gets, not going to do anth.");
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
        try {
            String msgNum = input.split(" ")[1];
            int taskIndex = Integer.parseInt(msgNum) - 1;
            if (taskIndex < 0 || taskIndex >= tasks.size()) {
                msgWithDivider("Invalid task number! Please provide an index between 1 and " + tasks.size() + ".");
                return;
            }
            Task task = tasks.get(taskIndex);
            if (isCompleted) {
                task.markCompleted();
                msgWithDivider("Yippy! " + (taskIndex + 1) + ": " + task + " has been mark completed.");
            } else {
                task.markUncompleted();
                msgWithDivider("Aww! " + (taskIndex + 1) + ": " + task + " has been marked uncompleted.");
            }
        } catch (NumberFormatException e) {
            msgWithDivider("what theee, your number is way too big!");
        }
    }

    public static void handleAddToDo(List<Task> tasks, String userMsg){
        String taskName = userMsg.split(" ", 2)[1];
        ToDo task = new ToDo(taskName);
        tasks.add(task);
        msgWithDivider("Gotcha boss, the task: " + task + " has been added!");
    }

    public static void handleAddDeadline(List<Task> tasks, String userMsg){
        String taskNameAndDeadline = userMsg.split(" ", 2)[1];
        String taskName = taskNameAndDeadline.split(" /by ")[0];
        String deadline = taskNameAndDeadline.split(" /by ")[1];

        Deadline task = new Deadline(taskName, deadline);
        tasks.add(task);
        msgWithDivider("Gotcha boss, the task: " + task + " has been added!");
    }

    public static void handleAddEvent(List<Task> tasks, String userMsg){
        String eventAndDuration = userMsg.split(" ", 2)[1];
        String eventName = eventAndDuration.split(" /from ")[0];
        String duration = eventAndDuration.split(" /from ")[1];
        String startTime = duration.split(" /to ")[0];
        String endTime = duration.split(" /to ")[1];

        Event task = new Event(eventName, startTime, endTime);
        tasks.add(task);
        msgWithDivider("Gotcha boss, the event: " + task + " has been added!");
    }
}
