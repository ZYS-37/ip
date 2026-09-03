package xerxes.parser;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.List;

import xerxes.storage.TaskStorage;
import xerxes.task.Deadline;
import xerxes.task.Event;
import xerxes.task.Task;
import xerxes.task.TaskList;
import xerxes.task.ToDo;

/**
 * Interprets user commands and performs the corresponding task-list operations.
 */
public class Parser {
    /** Strict date format accepted for deadline and event commands. */
    private static final DateTimeFormatter INPUT_FORMAT =
            DateTimeFormatter.ofPattern("d/M/uuuu")
                    .withResolverStyle(ResolverStyle.STRICT);

    /** Storage used when the user requests that tasks be saved. */
    private final TaskStorage taskStorage;

    /** Creates a parser that saves tasks through the given storage. */
    public Parser(TaskStorage taskStorage) {
        this.taskStorage = taskStorage;
    }

    /**
     * Processes one user command.
     *
     * @param input Command entered by the user.
     * @param tasks Task list to query or modify.
     * @return Result containing the response, error status, and exit status.
     */
    public CommandResult handleCommand(String input, TaskList tasks) {
        if (input.equals("bye")) {
            return success("Ciao, cya again", true);
        }
        if (input.equals("list")) {
            return success(tasks.toString());
        }
        if (input.equals("save")) {
            return handleSaveTasks(tasks);
        }
        if (input.equals("find") || input.startsWith("find ")) {
            return handleFindTasks(input, tasks);
        }
        if (input.matches("mark \\d+")) {
            return handleTaskStatus(input, tasks, true);
        }
        if (input.matches("unmark \\d+")) {
            return handleTaskStatus(input, tasks, false);
        }
        if (input.startsWith("todo ")) {
            return handleAddTodo(input, tasks);
        }
        if (input.startsWith("deadline ")) {
            return handleAddDeadline(input, tasks);
        }
        if (input.startsWith("event ")) {
            return handleAddEvent(input, tasks);
        }
        if (input.matches("delete \\d+")) {
            return handleDeleteTask(input, tasks);
        }
        return error("I dont gets, not going to do anth.");
    }

    /** Handles a find command. */
    private CommandResult handleFindTasks(String input, TaskList tasks) {
        String keyword = input.length() > 4 ? input.substring(5).trim() : "";
        if (keyword.isEmpty()) {
            return error("Please provide a keyword to search for.");
        }

        List<Task> matchingTasks = tasks.findMatchingTasks(keyword);
        if (matchingTasks.isEmpty()) {
            return success("No matching tasks found.");
        }

        StringBuilder message = new StringBuilder("Here are the matching tasks in your list:\n");
        for (int i = 0; i < matchingTasks.size(); i++) {
            message.append(i + 1).append(". ").append(matchingTasks.get(i));
            if (i < matchingTasks.size() - 1) {
                message.append("\n");
            }
        }
        return success(message.toString());
    }

    /** Handles a mark or unmark command. */
    private CommandResult handleTaskStatus(String input, TaskList tasks, boolean isCompleted) {
        try {
            int taskIndex = Integer.parseInt(input.split(" ")[1]) - 1;
            Task task = tasks.handleCompletionStatus(taskIndex, isCompleted);
            if (isCompleted) {
                return success("Yippy! " + (taskIndex + 1) + ": " + task
                        + " has been mark completed.");
            }
            return success("Awww " + (taskIndex + 1) + ": " + task
                    + " has been mark uncompleted. :(");
        } catch (NumberFormatException e) {
            return error("what theee, your number is way too big!");
        } catch (IllegalArgumentException e) {
            return error(e.getMessage());
        }
    }

    /** Handles a save command. */
    private CommandResult handleSaveTasks(TaskList tasks) {
        try {
            taskStorage.save(tasks);
            return success("Yr tasks have been saved!");
        } catch (IOException e) {
            return error("An error has occurred while saving!");
        }
    }

    /** Handles a todo command. */
    private CommandResult handleAddTodo(String input, TaskList tasks) {
        String description = input.substring(5).trim();
        if (description.isEmpty()) {
            return error("yoo the task name cannot be empty man.");
        }

        Task task = new ToDo(description);
        tasks.addTask(task);
        return success("Gotcha boss, the task: " + task + " has been added!");
    }

    /** Handles a deadline command. */
    private CommandResult handleAddDeadline(String input, TaskList tasks) {
        String taskNameAndDeadline = input.substring(9).trim();
        String byMarker = " /by ";
        int byIndex = taskNameAndDeadline.indexOf(byMarker);
        if (byIndex < 0) {
            return error("yoo yr format cmi must use : deadline <description> /by <time>");
        }

        String taskName = taskNameAndDeadline.substring(0, byIndex).trim();
        if (taskName.isEmpty()) {
            return error("yoo the task name cannot be empty man.");
        }

        try {
            LocalDate deadline = formatDate(taskNameAndDeadline
                    .substring(byIndex + byMarker.length()).trim());
            Deadline task = new Deadline(taskName, deadline);
            tasks.addTask(task);
            return success("Gotcha boss, the task: " + task + " has been added!");
        } catch (IllegalArgumentException e) {
            return error(e.getMessage());
        }
    }

    /** Handles an event command. */
    private CommandResult handleAddEvent(String input, TaskList tasks) {
        String eventAndDuration = input.substring(6).trim();
        String fromMarker = " /from ";
        String toMarker = " /to ";
        int fromIndex = eventAndDuration.indexOf(fromMarker);
        int toIndex = eventAndDuration.indexOf(toMarker, fromIndex + fromMarker.length());
        if (fromIndex < 0 || toIndex < 0 || toIndex <= fromIndex + fromMarker.length()) {
            return error("The format must be: event <description> /from <start> /to <end>");
        }

        String taskName = eventAndDuration.substring(0, fromIndex).trim();
        if (taskName.isEmpty()) {
            return error("yoo the task name cannot be empty man.");
        }

        try {
            LocalDate startTime = formatDate(eventAndDuration
                    .substring(fromIndex + fromMarker.length(), toIndex).trim());
            LocalDate endTime = formatDate(eventAndDuration
                    .substring(toIndex + toMarker.length()).trim());
            Event task = new Event(taskName, startTime, endTime);
            tasks.addTask(task);
            return success("Gotcha boss, the task: " + task + " has been added!");
        } catch (IllegalArgumentException e) {
            return error(e.getMessage());
        }
    }

    /** Handles a delete command. */
    private CommandResult handleDeleteTask(String input, TaskList tasks) {
        try {
            int index = Integer.parseInt(input.substring(7).trim()) - 1;
            Task task = tasks.deleteTask(index);
            return success("Task removed: " + task);
        } catch (IllegalArgumentException e) {
            return error(e.getMessage());
        }
    }

    /** Creates a successful result that keeps the application running. */
    private CommandResult success(String message) {
        return success(message, false);
    }

    /** Creates a successful result with the specified exit status. */
    private CommandResult success(String message, boolean shouldExit) {
        return new CommandResult(message, shouldExit, false);
    }

    /** Creates an error result that keeps the application running. */
    private CommandResult error(String message) {
        return new CommandResult(message, false, true);
    }

    /**
     * Parses a user-entered date in {@code d/M/yyyy} format using strict calendar validation.
     *
     * @param rawDate Date text entered by the user.
     * @return Parsed date.
     * @throws IllegalArgumentException If the date is invalid or uses the wrong format.
     */
    public static LocalDate formatDate(String rawDate) {
        try {
            return LocalDate.parse(rawDate, INPUT_FORMAT);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date/time format. Use: d/M/yyyy");
        }
    }
}
