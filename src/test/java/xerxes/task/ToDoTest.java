package xerxes.task;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class ToDoTest {
    @Test
    void toString_uncompletedTask_returnsUncheckedTodoFormat() {
        ToDo task = new ToDo("Read notes");

        assertEquals("[T][ ] Read notes", task.toString());
    }

    @Test
    void toString_completedTask_returnsCheckedTodoFormat() {
        ToDo task = new ToDo("Read notes");
        task.markCompleted();

        assertEquals("[T][X] Read notes", task.toString());
    }

    @Test
    void toString_completedThenUncompletedTask_returnsUncheckedTodoFormat() {
        ToDo task = new ToDo("Read notes");
        task.markCompleted();
        task.markUncompleted();

        assertEquals("[T][ ] Read notes", task.toString());
    }
}
