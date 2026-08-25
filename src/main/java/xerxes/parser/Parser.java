package xerxes.parser;

import xerxes.storage.TaskStorage;
import xerxes.task.TaskList;
import xerxes.task.Task;
import xerxes.task.ToDo;
import xerxes.task.Deadline;
import xerxes.task.Event;
import xerxes.ui.Ui;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.List;


public class Parser {
    private static final DateTimeFormatter INPUT_FORMAT =
            DateTimeFormatter.ofPattern("d/M/uuuu")
                    .withResolverStyle(ResolverStyle.STRICT);;
    private Ui ui;
    private TaskStorage taskStorage;

    public Parser(Ui ui, TaskStorage taskStorage) {
        this.taskStorage = taskStorage;
        this.ui = ui;
    }

    public boolean handleCommand(String input, TaskList tasks) {

        if (input.equals("bye")) {
            ui.showMessage("Ciao, cya again");
            return false;
        }

        if (input.equals("list")) {
            ui.showMessage(tasks.toString());
            return true;
        }
        if (input.equals("save")) {
            handleSaveTasks(taskStorage, tasks);
            return true;
        }

        if (input.equals("find") || input.startsWith("find ")) {
            handleFindTasks(input, tasks);
            return true;
        }

        if (input.matches("mark \\d+")) {
            handleTaskStatus(input, tasks, true);
            return true;
        }

        if (input.matches("unmark \\d+")) {
            handleTaskStatus(input, tasks, false);
            return true;
        }

        if (input.startsWith("todo ")) {
            handleAddTodo(input, tasks);
            return true;
        }

        if (input.startsWith("deadline ")) {
            handleAddDeadline(input, tasks);
            return true;
        }

        if (input.startsWith("event ")) {
            handleAddEvent(input, tasks);
            return true;
        }

        if (input.matches("delete \\d+")) {
            handleDeleteTask(input, tasks);
            return true;
        }

        ui.showError("I dont gets, not going to do anth.");
        return true;
    }

    /**
     * Searches the task list using the keyword in a find command and displays the results.
     *
     * @param input find command entered by the user.
     * @param tasks task list to search.
     */
    private void handleFindTasks(String input, TaskList tasks) {
        String keyword = input.length() > 4 ? input.substring(5).trim() : "";

        if (keyword.isEmpty()) {
            ui.showError("Please provide a keyword to search for.");
            return;
        }

        List<Task> matchingTasks = tasks.findMatchingTasks(keyword);
        ui.showMatchingTasks(matchingTasks);
    }

    private void handleTaskStatus(String input, TaskList tasks, boolean isCompleted) {
        try {
            String msgNum = input.split(" ")[1];
            int taskIndex = Integer.parseInt(msgNum) - 1;
            Task task = tasks.handleCompletionStatus(taskIndex, isCompleted);
            if (isCompleted) {
                ui.showMessage("Yippy! " + (taskIndex + 1) + ": " + task + " has been mark completed.");
            } else {
                ui.showMessage("Awww " + (taskIndex + 1) + ": " + task + " has been mark uncompleted. :(");
            }
        } catch (NumberFormatException e) {
            ui.showError("what theee, your number is way too big!");
        } catch (IllegalArgumentException e) {
            ui.showError(e.getMessage());
        }
    }

    public  void handleSaveTasks(TaskStorage taskStorage, TaskList tasks) {
        try {
            taskStorage.save(tasks);
            ui.showMessage("Yr tasks have been saved!");
        } catch (IOException e) {
            ui.showError("An error has occurred while saving!");
        }
    }

    private void handleAddTodo(String input, TaskList tasks) {
        String description = input.substring(5).trim();

        if (description.isEmpty()) {
            ui.showError("yoo the task name cannot be empty man.");
            return;
        }

        Task task = new ToDo(description);
        tasks.addTask(task);
        ui.showMessage("Gotcha boss, the task: " + task + " has been added!");
    }

    private void handleAddDeadline(String input, TaskList tasks) {
        String taskNameAndDeadline = input.substring(9).trim();

        String byMarker = " /by ";
        int byIndex = taskNameAndDeadline.indexOf(byMarker);
        if (byIndex < 0) {
            ui.showError("yoo yr format cmi must use : deadline <description> /by <time>");
            return;
        }

        String taskName = taskNameAndDeadline
                .substring(0, byIndex)
                .trim();

        if (taskName.isEmpty()) {
            ui.showError("yoo the task name cannot be empty man.");
            return;
        }

        try {
            LocalDate deadline = formatDate(taskNameAndDeadline
                    .substring(byIndex + byMarker.length())
                    .trim());
            Deadline task = new Deadline(taskName, deadline);
            tasks.addTask(task);
            ui.showMessage("Gotcha boss, the task: " + task + " has been added!");
        } catch (IllegalArgumentException e) {
            ui.showError(e.getMessage());
        }
    }

    private void handleAddEvent(String input, TaskList tasks) {
        String eventAndDuration = input.substring(6).trim();

        String fromMarker = " /from ";
        String toMarker = " /to ";

        int fromIndex = eventAndDuration.indexOf(fromMarker);
        int toIndex = eventAndDuration.indexOf(
                toMarker,
                fromIndex + fromMarker.length());

        if (fromIndex < 0 || toIndex < 0 || toIndex <= fromIndex + fromMarker.length()) {
            ui.showError("The format must be: event <description> /from <start> /to <end>");
            return;
        }

        String taskName = eventAndDuration
                .substring(0, fromIndex)
                .trim();

        if (taskName.isEmpty()) {
            ui.showError("yoo the task name cannot be empty man.");
            return;
        }

        try {
            LocalDate startTime = formatDate(eventAndDuration
                    .substring(fromIndex + fromMarker.length(), toIndex)
                    .trim());

            LocalDate endTime = formatDate(eventAndDuration
                    .substring(toIndex + toMarker.length())
                    .trim());
            Event task = new Event(taskName, startTime, endTime);
            tasks.addTask(task);
            ui.showMessage("Gotcha boss, the task: " + task + " has been added!");
        } catch (IllegalArgumentException e) {
            ui.showError(e.getMessage());
        }
    }

    private void handleDeleteTask(String input, TaskList tasks) {
        try {
            int index = Integer.parseInt(input.substring(7).trim()) - 1;
            Task task = tasks.deleteTask(index);
            ui.showMessage("Task removed: " + task);
        } catch (IllegalArgumentException e) {
            ui.showError(e.getMessage());
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
