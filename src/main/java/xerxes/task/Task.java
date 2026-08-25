package xerxes.task;

/**
 * Represents a task with a description and completion status.
 * Subclasses add task-specific details such as a deadline or event duration.
 */
public class Task {
    /** Description supplied by the user for this task. */
    private String taskName;

    /** Whether this task has been completed. */
    private boolean isCompleted;

    /**
     * Creates an incomplete task with the given description.
     *
     * @param taskName Description of the task.
     */
    public Task(String taskName) {
        this.taskName = taskName;
    }

    /** Marks this task as completed. */
    public void markCompleted() {
        this.isCompleted = true;
    }

    /** Marks this task as incomplete. */
    public void markUncompleted() {
        this.isCompleted = false;
    }

    /**
     * Returns whether this task has been completed.
     *
     * @return True if the task is completed, otherwise false.
     */
    public boolean getIsCompleted() {
        return this.isCompleted;
    }

    /**
     * Returns the task description.
     *
     * @return Description of this task.
     */
    public String getTaskName() {
        return this.taskName;
    }

    /**
     * Returns a display representation containing this task's completion status and description.
     *
     * @return Formatted task description.
     */
    @Override
    public String toString() {
        if (this.isCompleted) {
            return "[X] " + this.taskName;
        } else {
            return "[ ] " + this.taskName;
        }
    }
}
