import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Objects;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;

public class Xerxes {
    public static final String DIVIDER = "____________________________________________________________";
    public static final String BANNER = "__  __                        \n"
            + "\\ \\/ /  ___   _ __ __  __  ___  ___ \n"
            + " \\  /  / _ \\| '__|\\ \\/ / / _ \\/ __|\n"
            + " /  \\ |  __/| |    >  < |  __/\\__ \\\n"
            + "/_/\\_\\ \\___||_|   /_/\\_\\ \\___||___/";
    private static final String SAVE_FILE_PATH = "data/Xerxes.txt";
    private static final DateTimeFormatter INPUT_FORMAT =
            DateTimeFormatter.ofPattern("d/M/uuuu");

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        TaskStorage taskStorage = new TaskStorage();
        initialMsg();
        List<Task> tasks = loadSaveFile(taskStorage);

        boolean isActive = true;
        while (isActive) {
            String userMsg = scanner.nextLine();

            if (Objects.equals(userMsg, "bye")) {
                isActive = false;
            } else if (Objects.equals(userMsg, "list")) {
                printTaskList(tasks);
            } else if (Objects.equals(userMsg, "save")) {
                saveTasks(taskStorage, tasks);
            } else if (userMsg.matches("mark \\d+")) {
                handleTaskStatus(tasks, userMsg, true);
            } else if (userMsg.matches("unmark \\d+")) {
                handleTaskStatus(tasks, userMsg, false);
            } else if (userMsg.startsWith("todo ")) {
                handleAddToDo(tasks, userMsg);
            } else if (userMsg.startsWith("deadline ")) {
                handleAddDeadline(tasks, userMsg);
            } else if (userMsg.startsWith("event ")) {
                handleAddEvent(tasks, userMsg);
            } else if (userMsg.matches("delete \\d+")) {
                handleDeletion(tasks, userMsg);
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

    public static void handleAddToDo(List<Task> tasks, String userMsg) {
        String taskName = userMsg.substring(5).trim();
        if (taskName.isEmpty()) {
            msgWithDivider("yoo the task name cannot be empty man");
            return;
        }
        ToDo task = new ToDo(taskName);
        tasks.add(task);
        msgWithDivider("Gotcha boss, the task: " + task + " has been added!");
    }

    public static void handleAddDeadline(List<Task> tasks, String userMsg) {
        String taskNameAndDeadline = userMsg.substring(9).trim();
        if (!taskNameAndDeadline.contains(" /by ")) {
            msgWithDivider("yoo yr format cmi must use : deadline <description> /by <time>");
            return;
        }
        String[] parts = taskNameAndDeadline.split(" /by ");
        String taskName = parts[0];
        LocalDate deadline = formatDate(parts[1]);

        if (taskName.isEmpty()) {
            msgWithDivider("yoo the both the task name and deadline must be there");
            return;
        }
        Deadline task = new Deadline(taskName, deadline);
        tasks.add(task);
        msgWithDivider("Gotcha boss, the task: " + task + " has been added!");
    }

    public static void handleAddEvent(List<Task> tasks, String userMsg) {
        String eventAndDuration = userMsg.substring(6).trim();

        String fromMarker = " /from ";
        String toMarker = " /to ";

        int fromIndex = eventAndDuration.indexOf(fromMarker);
        int toIndex = eventAndDuration.indexOf(
                toMarker,
                fromIndex + fromMarker.length());


        if (fromIndex < 0 || toIndex < 0 || toIndex <= fromIndex + fromMarker.length()) {
            msgWithDivider(
                    "The format must be: event <description> /from <start> /to <end>");
            return;
        }

        String taskName = eventAndDuration
                .substring(0, fromIndex)
                .trim();
        try {
            LocalDate startTime = formatDate(eventAndDuration
                    .substring(fromIndex + fromMarker.length(), toIndex)
                    .trim());

            LocalDate endTime = formatDate(eventAndDuration
                    .substring(toIndex + toMarker.length())
                    .trim());
            if (taskName.isEmpty()) {
                msgWithDivider("yoo you must have a task name, a start time and an end time");
            }

            Event task = new Event(taskName, startTime, endTime);
            tasks.add(task);

            msgWithDivider("Gotcha boss, the event: " + task + " has been added!");
        } catch (IllegalArgumentException e) {
            msgWithDivider(e.getMessage());
        }


    }

    public static void handleDeletion(List<Task> tasks, String userMsg) {
       try {
            int index = Integer.parseInt(userMsg.substring(7).trim()) - 1;

            if (index < 0 || index >= tasks.size()) {
                msgWithDivider("Invalid task number! Please provide an index between 1 and " + tasks.size() + ".");
                return;
            }

            Task task = tasks.get(index - 1);
            msgWithDivider("got it! " + task + " has been removed");
            tasks.remove(index - 1);
        } catch (NumberFormatException e) {
            msgWithDivider("what theee, your delete index is way too big!");
        }
    }

    public static List<Task> loadSaveFile(TaskStorage taskStorage) {
        // Handle creation of directory and file if it does not exist
        Path filePath = Paths.get(SAVE_FILE_PATH);
        try {
            if (filePath.getParent() != null) {
                Files.createDirectories(filePath.getParent());
            }

            if (Files.notExists(filePath)) {
                Files.createFile(filePath);
                System.out.println("Directory and file have been created");
            }
        } catch (IOException e) {
            System.err.println("Error has occurred" + e.getMessage());
        }
        try {
            List<Task> tasks = taskStorage.load(filePath);
            msgWithDivider("Tasks have been loaded!");
            return tasks;
        } catch (IOException e) {
            System.err.println("Unable to load save file, starting with an empty task list.");
            return new ArrayList<>();
        } catch (IllegalArgumentException e) {
            System.err.println("Save file is corrupted: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public static void saveTasks(TaskStorage taskStorage, List<Task> tasks) {
        try {
            taskStorage.save(tasks, SAVE_FILE_PATH);
            msgWithDivider("Yr tasks have been saved!");
        } catch (IOException e) {
            System.err.println("An error has occurred while saving!");
        }
    }

    public static LocalDate formatDate(String rawDate) throws DateTimeParseException {
        try {
            return LocalDate.parse(rawDate, INPUT_FORMAT);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date/time format. Use: d/M/yyyy");
        }
    }
}
