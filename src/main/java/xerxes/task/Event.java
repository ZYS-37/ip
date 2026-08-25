package xerxes.task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Event extends Task {
    private LocalDate startTime;
    private LocalDate endTime;
    private static final DateTimeFormatter DISPLAY_FORMAT = DateTimeFormatter.ofPattern("MMM dd yyyy");

    public Event(String taskName, LocalDate startTime, LocalDate endTime) {
        super(taskName);
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public LocalDate getStartTime() {
        return this.startTime;
    }

    public LocalDate getEndTime() {
        return this.endTime;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: "
                + this.startTime.format(DISPLAY_FORMAT) + " to: "
                + this.endTime.format(DISPLAY_FORMAT) + " )";
    }

}
