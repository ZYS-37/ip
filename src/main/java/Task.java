public class Task {
    private String taskName;
    private boolean isCompleted;

    public Task(String taskName) {
        this.taskName = taskName;
    }

    public void markCompleted() {
        this.isCompleted = true;
    }

    public void markUncompleted() {
        this.isCompleted = false;
    }

    @Override
    public String toString() {
        if (this.isCompleted) {
            return "[X] " + this.taskName;
        } else {
            return "[ ] " + this.taskName;
        }
    }
}
