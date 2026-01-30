package babby.storage;

import org.junit.jupiter.api.Test;
import babby.task.TaskList;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class StorageTest {
    @Test
    public void testInitiateAndSaveParse() throws Exception {
        String tmpPath = "data/test_tasks.txt";
        // create a temporary file with two tasks in encoded format
        File f = new File(tmpPath);
        if (f.exists()) f.delete();
        f.createNewFile();

        FileWriter fw = new FileWriter(f);
        fw.write("T | 0 | alpha" + System.lineSeparator());
        fw.write("T | 1 | beta" + System.lineSeparator());
        fw.close();

        Storage storage = new Storage(tmpPath);
        TaskList loaded = storage.parseTasks();
        assertEquals(2, loaded.size());

        // modify and save
        loaded.remove(0);
        storage.saveTasks(loaded);
        TaskList reloaded = storage.parseTasks();
        assertEquals(1, reloaded.size());

        // cleanup
        f.delete();
    }
}
