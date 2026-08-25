package xerxes;

import java.io.IOException;

import xerxes.parser.Parser;
import xerxes.storage.TaskStorage;
import xerxes.task.TaskList;
import xerxes.ui.Ui;

public class Xerxes {
    private static final String SAVE_FILE_PATH = "data/Xerxes.txt";
    private final TaskStorage taskStorage;
    private final TaskList tasks;
    private final Ui ui;
    private final Parser parser;

    public Xerxes(String filePath) {
        this.ui = new Ui();
        this.taskStorage = new TaskStorage(filePath);
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

    public void run() {
        ui.showInitialMessage();
        boolean isActive = true;
        while (isActive) {
            String input = ui.readCommand();
            isActive = parser.handleCommand(input, tasks);
        }
        ui.close();
    }

    public static void main(String[] args) {
        new Xerxes(SAVE_FILE_PATH).run();
    }
}
