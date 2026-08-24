import java.io.IOException;
import java.sql.Array;
import java.util.Objects;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.FileWriter;

public class TaskStorage {
    public void save(List<Task> tasks, String saveFilePath) {
        File saveFile = new File(saveFilePath);

        try {
            FileWriter saveFileWriter = new FileWriter(saveFile);
            for (Task task: tasks) {
                saveFileWriter.write(encode(task));
            }
            saveFileWriter.close();

        } catch (IOException e) {
            System.err.println("An error has occurred " + e.getMessage());
        }
    }

    public List<Task> load(Path saveFilePath) {
        try {
            List<Task> tasks = new ArrayList<>();
            List<String> lines = Files.readAllLines(saveFilePath);
            for (int lineNum = 0; lineNum < lines.size(); lineNum++) {
                String line = lines.get(lineNum);

                if (line.isBlank()) {
                    continue;
                }
                try {
                    tasks.add(decode(line));
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException(
                            "Malformed save file at line " + (lineNum + 1), e);
                }
            }
            return tasks;

        } catch (IOException e) {
            System.err.println("An error has occurred " + e.getMessage());
        }

    }
    public String encode(Task task) {}
    private Task decode(String line) {}

    public List<String> splitEscape(String line) {

    }
}
