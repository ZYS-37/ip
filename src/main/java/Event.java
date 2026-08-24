public class Event extends Task {
    private String startTime;
    private String endTime;

    public Event(String taskName, String startTime, String endTime) {
        super(taskName);
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getStartTime() {
        return this.startTime;
    }

    public String getEndTime() {
        return this.endTime;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + startTime + " to: " + endTime + " )";
    }

}
