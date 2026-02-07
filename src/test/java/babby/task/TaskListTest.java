package babby.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class TaskListTest {
    @Test
    public void testEmptyListBehavior() {
        TaskList list = new TaskList();
        assertTrue(list.isEmpty());
        list.add(new ToDo("a"));
        assertEquals(1, list.size());
        Task removed = list.remove(0);
        assertEquals("a", removed.getTitle());
        assertTrue(list.isEmpty());
    }

    @Test
    public void testFindNoMatch() {
        TaskList list = new TaskList();
        list.add(new ToDo("alpha"));
        // calling find should not throw
        list.find("beta");
    }
}
