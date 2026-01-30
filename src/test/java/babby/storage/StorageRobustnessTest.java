package babby.storage;

import babby.task.TaskList;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;

import static org.junit.jupiter.api.Assertions.*;

public class StorageRobustnessTest {
    @Test
    public void testParseSkipsMalformedLinesAndParsesValidOnes() throws Exception {
        String path = "data/tmp_storage_robust.txt";
        File f = new File(path);
        if (f.exists()) f.delete();
        f.createNewFile();

        FileWriter fw = new FileWriter(f);
        // Valid ToDo
        fw.write("T | 0 | keep" + System.lineSeparator());
        // Malformed line (missing fields)
        fw.write("BAD LINE" + System.lineSeparator());
        // Valid Deadline
        fw.write("D | 1 | dohomework | 2025-12-31T23:59:00" + System.lineSeparator());
        // Event with invalid dates (parser should catch and skip)
        fw.write("E | 0 | party | not-a-date | not-a-date" + System.lineSeparator());
        fw.close();

        Storage storage = new Storage(path);
        TaskList list = storage.parseTasks();

        // Should have parsed two valid tasks (ToDo and Deadline) and skipped malformed ones
        assertEquals(2, list.size());

        // Cleanup
        f.delete();
    }
}
