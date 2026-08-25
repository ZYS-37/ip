package xerxes.task;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TaskList implements Iterable<Task> {
    private final List<Task> tasks;

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
