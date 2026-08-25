package xerxes.storage;

import xerxes.task.Task;
import xerxes.task.ToDo;
import xerxes.task.Deadline;
import xerxes.task.Event;
import xerxes.task.TaskList;

import java.io.IOException;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TaskStorage {
    private String savefilePath;

    public TaskStorage (String savefilePath) {
        this.savefilePath = savefilePath;
    }


    public void save(TaskList tasks) throws IOException{
        File saveFile = new File(this.savefilePath);
        BufferedWriter saveFileWriter = new BufferedWriter(new FileWriter(saveFile));
        for (Task task: tasks) {
            saveFileWriter.write(encode(task));
            saveFileWriter.newLine();
        }
        saveFileWriter.close();

    }

    public List<Task> load() throws IOException {
        Path filePath = Path.of(this.savefilePath);
        try {
            if (filePath.getParent() != null) {
                Files.createDirectories(filePath.getParent());
            }

            if (Files.notExists(filePath)) {
                Files.createFile(filePath);
            }
        } catch (IOException e) {
            System.err.println("Error has occurred" + e.getMessage());
        }
        List<Task> tasks = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(filePath);

            for (int lineNum = 0; lineNum < lines.size(); lineNum++) {
                String line = lines.get(lineNum);

                if (line.isBlank()) {
                    continue;
                }
                try {
                    tasks.add(decode(line));
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException(
                            "Malformed save file at line " + (lineNum + 1) +": "+
                                    e.getMessage(), e);
                }
            }
        } catch (IOException e){
            throw new IOException("Unable to load save file, starting with an empty task list.");
        }
        return tasks;
    }

    private String encodeField(String field) {
        return field.replace("\\", "\\\\")
                .replace("|", "\\|");
    }

    private String encode(Task task) {
        String status = task.getIsCompleted() ? "1" : "0";
        String description = encodeField(task.getTaskName());

        return switch (task) {
            case ToDo ignored-> String.join(
                    "|",
                    "TODO",
                    status,
                    description);
            case Deadline deadline-> String.join(
                    "|",
                    "DEADLINE",
                    status,
                    description,
                    encodeField(deadline.getDeadline().format(DateTimeFormatter.ISO_LOCAL_DATE)));
            case Event event-> String.join(
                    "|",
                    "EVENT",
                    status,
                    description,
                    encodeField(event.getStartTime().format(DateTimeFormatter.ISO_LOCAL_DATE)),
                    encodeField(event.getEndTime().format(DateTimeFormatter.ISO_LOCAL_DATE)));

            case null -> throw new IllegalArgumentException("Task cannot be null");

            default -> throw new IllegalArgumentException(
                    "Unknown task type: " + task.getClass().getName());
        };
    }

    private Task decode(String line) {
        List<String> fields = splitEscape(line);
        if (fields.size() < 3) {
            throw new IllegalArgumentException("Not enough fields");
        }

        String type = fields.get(0);
        boolean isCompleted = switch (fields.get(1)) {
            case "0" -> false;
            case "1" -> true;
            default -> throw new IllegalArgumentException("Invalid completion status");
        };

        String description = fields.get(2);

        Task task = switch (type) {
            case "TODO" -> {
                requireFieldCount(fields, 3);
                yield new ToDo(description);
            }
            case "DEADLINE" -> {
                requireFieldCount(fields, 4);
                yield new Deadline(description,
                        parseSavedDate(fields.get(3), "deadline"));
            }
            case "EVENT" -> {
                requireFieldCount(fields, 5);
                yield new Event(description,
                        parseSavedDate(fields.get(3), "event start time"),
                        parseSavedDate(fields.get(4), "event end time"));
            }
            default -> throw new IllegalArgumentException("Unknown task type: " + type);
        };

        if (isCompleted) {
            task.markCompleted();
        }

        return task;
    }

    private void requireFieldCount(List<String> fields, int expected) {
        if (fields.size() != expected) {
            throw new IllegalArgumentException("Incorrect number of fields");
        }
    }

    public List<String> splitEscape(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder currentField = new StringBuilder();
        boolean isEscaping = false;

        // Handle Escapes
        for (char character: line.toCharArray()) {
            if (isEscaping) {
                if (character != '|' && character != '\\') {
                    throw new IllegalArgumentException(
                            "Invalid Escape sequence : \\" + character);
                }
                currentField.append(character);
                isEscaping = false;
            } else if (character == '\\') {
                isEscaping = true;
            } else if (character == '|') {
                fields.add(currentField.toString());
                currentField.setLength(0);
            } else {
                currentField.append(character);
            }
        }

        if (isEscaping) {
            throw new IllegalArgumentException(
                    "Save-file line ends with an incomplete escape");
        }

        fields.add(currentField.toString());
        return fields;
    }
    private LocalDate parseSavedDate(String value, String fieldName) {
        try{
            return LocalDate.parse(value,DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(
                    "Invalid " + fieldName  + ": " + value, e);
        }
    }
}
