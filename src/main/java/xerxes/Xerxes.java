package xerxes;

import xerxes.parser.Parser;
import xerxes.storage.TaskStorage;
import xerxes.task.TaskList;
import xerxes.ui.Ui;

import java.io.IOException;

public class Xerxes {
    private static final String SAVE_FILE_PATH = "data/Xerxes.txt";
    private TaskStorage taskStorage;
    private TaskList tasks;
    private Ui ui;
    private Parser parser;

    public Xerxes(String filePath) {
        this.ui = new Ui();
        this.taskStorage = new TaskStorage(filePath);

        try {
            tasks = new TaskList(taskStorage.load());
            ui.showTopMessage("Save file has been loaded");
        } catch (IOException e) {
            ui.showTopError("Save file has failed to load: " + e.getMessage());
            tasks = new TaskList();
        } catch (IllegalArgumentException e) {
            ui.showTopError("Save file has been corrupted: " + e.getMessage());
            tasks = new TaskList();
        }
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
