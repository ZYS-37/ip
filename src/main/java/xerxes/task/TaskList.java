package xerxes.task;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Manages an ordered collection of tasks.
 */
public class TaskList implements Iterable<Task>{
    /** Tasks in the order they were added or loaded. */
    private List<Task> tasks;

    /** Creates an empty task list. */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Creates a task list containing a copy of the supplied tasks.
     *
     * @param tasks tasks to add to this list
     */
    public TaskList(List<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);

    }

    /**
     * Changes the completion status of the task at the given zero-based index.
     *
     * @param taskIndex zero-based index of the task to update
     * @param isCompleted new completion status
     * @return the updated task
     * @throws IllegalArgumentException if {@code taskIndex} is outside this list
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
     * @param taskIndex zero-based index of the task to remove
     * @return the removed task
     * @throws IllegalArgumentException if {@code taskIndex} is outside this list
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
     * @param index zero-based index to validate
     * @throws IllegalArgumentException if the index is outside this list
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
     * @param task task to add
     */
    public void addTask(Task task) {
        this.tasks.add(task);
    }
    /**
     * Returns all tasks as a numbered, line-separated list.
     *
     * @return formatted representation of this task list
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
     * @return iterator over the contained tasks
     */
    @Override
    public Iterator<Task> iterator() {
        return tasks.iterator();
    }
}
