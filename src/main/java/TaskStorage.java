import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.FileWriter;
import java.io.BufferedWriter;

public class TaskStorage {
    public void save(List<Task> tasks, String saveFilePath) {
        File saveFile = new File(saveFilePath);

        try {
            BufferedWriter saveFileWriter = new BufferedWriter(new FileWriter(saveFile));
            for (Task task: tasks) {
                saveFileWriter.write(encode(task));
                saveFileWriter.newLine();
            }
            saveFileWriter.close();

        } catch (IOException e) {
            System.err.println("An error has occurred " + e.getMessage());
        }
    }

    public List<Task> load(Path saveFilePath) throws IOException {
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
                        "Malformed save file at line " + (lineNum + 1) +
                        e.getMessage(), e);
            }
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
                    encodeField(deadline.getDeadline()));
            case Event event-> String.join(
                    "|",
                    "EVENT",
                    status,
                    description,
                    encodeField(event.getStartTime()),
                    encodeField(event.getEndTime()));

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
                yield new Deadline(description, fields.get(3));
            }
            case "EVENT" -> {
                requireFieldCount(fields, 5);
                yield new Event(description, fields.get(3), fields.get(4));
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
}
