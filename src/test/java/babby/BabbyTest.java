package babby;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

public class BabbyTest {
    @Test
    public void testRunningState() {
        Babby babby = new Babby("data/tasks.txt");
        assertNotNull(babby.getStorage(), "Storage should be initialized");
        assertNotNull(babby.getTaskList(), "TaskList should be initialized");
        assertEquals(0, babby.getTaskList().size(), "New Babby should start with empty task list");
    }
}
