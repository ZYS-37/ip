package xerxes.task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Represents a task scheduled over a start and end date.
 */
public class Event extends Task {
    /** Date on which this event begins. */
    private LocalDate startTime;

    /** Date on which this event ends. */
    private LocalDate endTime;

    /** Format used when displaying event dates to the user. */
    private static final DateTimeFormatter DISPLAY_FORMAT = DateTimeFormatter.ofPattern("MMM dd yyyy");

    /**
     * Creates an incomplete event task.
     *
     * @param taskName Description of the event.
     * @param startTime Date on which the event starts.
     * @param endTime Date on which the event ends.
     */
    public Event(String taskName, LocalDate startTime, LocalDate endTime) {
        super(taskName);
        this.startTime = startTime;
        this.endTime = endTime;
    }

    /**
     * Returns the event start date.
     *
     * @return Start date of the event.
     */
    public LocalDate getStartTime() {
        return this.startTime;
    }

    /**
     * Returns the event end date.
     *
     * @return End date of the event.
     */
    public LocalDate getEndTime() {
        return this.endTime;
    }

    /**
     * Returns a display representation including this event's duration.
     *
     * @return Formatted event task description.
     */
    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: "
                + this.startTime.format(DISPLAY_FORMAT) + " to: "
                + this.endTime.format(DISPLAY_FORMAT) + " )";
    }

}
