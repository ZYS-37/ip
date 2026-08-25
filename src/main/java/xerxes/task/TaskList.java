package xerxes.task;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class TaskList implements Iterable<Task>{
    private List<Task> tasks;

    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    public TaskList(List<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);

    }

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

    public Task deleteTask(int taskIndex) {
        validateIndex(taskIndex);
        Task task = tasks.get(taskIndex);
        tasks.remove(taskIndex);
        return task;
    }

    public void validateIndex(int index) {
        if (index < 0 || index >= tasks.size()) {
            throw new IllegalArgumentException(
                    "Invalid task number! Please provide an index between 1 and " + tasks.size() + ".");
        }
    }

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

    @Override
    public Iterator<Task> iterator() {
        return tasks.iterator();
    }
}
