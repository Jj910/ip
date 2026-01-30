package babby.task;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

public class DeadlineEventTest {
    @Test
    public void testDeadlineEncodingAndDisplay() {
        LocalDateTime dt = LocalDateTime.of(2025,12,31,23,59);
        Deadline d = new Deadline("submit", dt);
        assertEquals("submit", d.getTitle());
        assertFalse(d.getIsComplete());
        // Encoded string uses ISO_LOCAL_DATE_TIME
        assertTrue(d.toEncodedString().startsWith("D | 0 | submit | "));
        assertTrue(d.toString().contains("(By: "));
    }

    @Test
    public void testEventEncodingAndDisplay() {
        LocalDateTime from = LocalDateTime.of(2025,1,1,14,0);
        LocalDateTime to = LocalDateTime.of(2025,1,1,16,0);
        Event e = new Event("party", from, to);
        assertEquals("party", e.getTitle());
        assertFalse(e.getIsComplete());
        assertTrue(e.toEncodedString().startsWith("E | 0 | party | "));
        assertTrue(e.toString().contains("(From: "));
    }
}
