package babby.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.FileWriter;

import org.junit.jupiter.api.Test;

import babby.task.Friend;
import babby.task.TaskList;

public class StorageTest {
    @Test
    public void testInitiateAndSaveParse() throws Exception {
        String tmpPath = "data/test_tasks.txt";
        // create a temporary file with two tasks in encoded format
        File f = new File(tmpPath);
        if (f.exists()) {
            f.delete();
        }
        f.createNewFile();

        FileWriter fw = new FileWriter(f);
        fw.write("T | 0 | alpha" + System.lineSeparator());
        fw.write("T | 1 | beta" + System.lineSeparator());
        fw.write("F | 1 | Charlie | 12345" + System.lineSeparator());
        fw.close();

        Storage storage = new Storage(tmpPath);
        TaskList loaded = storage.parseTasks();
        assertEquals(3, loaded.size());
        Friend friend = (Friend) loaded.get(2);
        assertEquals("Charlie", friend.getTitle());
        assertEquals("F | 1 | Charlie | 12345", friend.toEncodedString());

        // modify and save
        loaded.remove(0);
        storage.saveTasks(loaded);
        TaskList reloaded = storage.parseTasks();
        assertEquals(2, reloaded.size());
        assertEquals("F | 1 | Charlie | 12345", ((Friend) reloaded.get(1)).toEncodedString());

        // cleanup
        f.delete();
    }
}
