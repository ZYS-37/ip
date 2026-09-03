package xerxes;

import java.io.IOException;

import xerxes.parser.Parser;
import xerxes.storage.TaskStorage;
import xerxes.task.TaskList;
import xerxes.ui.Ui;

/**
 * Coordinates the user interface, command parser, task list, and persistent storage
 * for the Xerxes task manager.
 */
public class Xerxes {
    /** Default location used to persist tasks when the application starts normally. */
    private static final String SAVE_FILE_PATH = "data/Xerxes.txt";

    /** Stores and retrieves the application's tasks. */
    private final TaskStorage taskStorage;

    /** Contains the tasks managed during this application session. */
    private final TaskList tasks;

    /** Handles console input and output. */
    private final Ui ui;

    /** Interprets commands entered by the user. */
    private final Parser parser;

    /**
     * Creates an application instance and loads tasks from the specified file.
     * If the file cannot be read or contains malformed task data, the application
     * starts with an empty task list instead.
     *
     * @param filePath Path of the task save file.
     */
    public Xerxes() {
        this.ui = new Ui();
        this.taskStorage = new TaskStorage(SAVE_FILE_PATH);
        TaskList loadedTasks;

        try {
            loadedTasks = new TaskList(taskStorage.load());
            ui.showTopMessage("Save file has been loaded");
        } catch (IOException e) {
            ui.showTopError("Save file has failed to load: " + e.getMessage());
            loadedTasks = new TaskList();
        } catch (IllegalArgumentException e) {
            ui.showTopError("Save file has been corrupted: " + e.getMessage());
            loadedTasks = new TaskList();
        }
        this.tasks = loadedTasks;
        this.parser = new Parser(ui, taskStorage);
    }

    /**
     * Displays the welcome message and repeatedly processes commands until the user exits.
     */
    public void run() {
        ui.showInitialMessage();
        boolean isActive = true;
        while (isActive) {
            String input = ui.readCommand();
            isActive = parser.handleCommand(input, tasks);
        }
        ui.close();
    }

    /**
     * Starts the Xerxes application using the default save-file location.
     *
     * @param args Command-line arguments, which are not currently used.
     */
    public static void main(String[] args) {
        new Xerxes().run();
    }
}
