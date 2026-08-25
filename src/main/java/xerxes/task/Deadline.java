package xerxes.task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Represents a task that must be completed by a particular date.
 */
public class Deadline extends Task {
    /** Date by which this task should be completed. */
    private final LocalDate deadline;

    /** Format used when displaying the deadline to the user. */
    private static final DateTimeFormatter DISPLAY_FORMAT = DateTimeFormatter.ofPattern("MMM dd yyyy");

    /**
     * Creates an incomplete deadline task.
     *
     * @param taskName Description of the task.
     * @param deadline Date by which the task should be completed.
     */
    public Deadline(String taskName, LocalDate deadline) {
        super(taskName);
        this.deadline = deadline;
    }

    /**
     * Returns the deadline date.
     *
     * @return Deadline date for this task.
     */
    public LocalDate getDeadline() {
        return this.deadline;
    }

    /**
     * Returns a display representation including this task's deadline.
     *
     * @return Formatted deadline task description.
     */
    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: "
                + this.deadline.format(DISPLAY_FORMAT) + ")";
    }

}
