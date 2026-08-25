package xerxes.task;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/**
 * Manages an ordered collection of tasks.
 */
public class TaskList implements Iterable<Task>{
    /** Tasks in the order they were added or loaded. */
    private final List<Task> tasks;

    /** Creates an empty task list. */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Creates a task list containing a copy of the supplied tasks.
     *
     * @param tasks Tasks to add to this list.
     */
    public TaskList(List<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);

    }

    /**
     * Changes the completion status of the task at the given zero-based index.
     *
     * @param taskIndex Zero-based index of the task to update.
     * @param isCompleted New completion status.
     * @return The updated task.
     * @throws IllegalArgumentException If {@code taskIndex} is outside this list.
     */
    public Task handleCompletionStatus(int taskIndex, boolean isCompleted) {
        validateIndex(taskIndex);

        Task task = tasks.get(taskIndex);

        if (isCompleted) {
            task.markCompleted();
        } else {
            task.markUncompleted();
        }
        return task;
    }

    /**
     * Removes and returns the task at the given zero-based index.
     *
     * @param taskIndex Zero-based index of the task to remove.
     * @return The removed task.
     * @throws IllegalArgumentException If {@code taskIndex} is outside this list.
     */
    public Task deleteTask(int taskIndex) {
        validateIndex(taskIndex);
        Task task = tasks.get(taskIndex);
        tasks.remove(taskIndex);
        return task;
    }

    /**
     * Validates that an index identifies a task in this list.
     *
     * @param index Zero-based index to validate.
     * @throws IllegalArgumentException If the index is outside this list.
     */
    public void validateIndex(int index) {
        if (index < 0 || index >= tasks.size()) {
            throw new IllegalArgumentException(
                    "Invalid task number! Please provide an index between 1 and " + tasks.size() + ".");
        }
    }

    /**
     * Appends a task to this list.
     *
     * @param task Task to add.
     */
    public void addTask(Task task) {
        this.tasks.add(task);
    }

    /**
     * Returns tasks whose descriptions contain the given keyword, ignoring case.
     *
     * @param keyword keyword to search for.
     * @return matching tasks in their original list order.
     */
    public List<Task> findMatchingTasks(String keyword) {
        List<Task> matchingTasks = new ArrayList<>();

        if (keyword == null || keyword.isBlank()) {
            return matchingTasks;
        }

        String normalizedKeyword = keyword.toLowerCase(Locale.ROOT);
        for (Task task : tasks) {
            String normalizedTaskName = task.getTaskName().toLowerCase(Locale.ROOT);
            if (normalizedTaskName.contains(normalizedKeyword)) {
                matchingTasks.add(task);
            }
        }

        return matchingTasks;
    }

    /**
     * Returns all tasks as a numbered, line-separated list.
     *
     * @return Formatted representation of this task list.
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        int i = 1;
        for (Task task : tasks) {
            if (i > 1) {
                result.append("\n");
            }
            result.append(i).append(": ").append(task);
            i++;
        }
        return result.toString();
    }

    /**
     * Returns an iterator over the tasks in list order.
     *
     * @return Iterator over the contained tasks.
     */
    @Override
    public Iterator<Task> iterator() {
        return tasks.iterator();
    }
}
