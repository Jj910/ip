package babby.task;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ToDoTest {
    @Test
    public void testToDoInitializationAndEncoding() {
        ToDo todo = new ToDo("buy milk");
        assertEquals("buy milk", todo.getTitle());
        assertFalse(todo.getIsComplete());
        assertEquals("T | 0 | buy milk", todo.toEncodedString());
        assertTrue(todo.toString().startsWith("[T] "));
    }

    @Test
    public void testToDoCompletionToggle() {
        ToDo todo = new ToDo("task");
        todo.setDone();
        assertTrue(todo.getIsComplete());
        todo.setToDo();
        assertFalse(todo.getIsComplete());
    }
}
