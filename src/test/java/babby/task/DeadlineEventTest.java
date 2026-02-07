package babby.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

public class DeadlineEventTest {
    @Test
    public void testDeadlineEncodingAndDisplay() {
        LocalDateTime dt = LocalDateTime.of(2025, 12, 31, 23, 59);
        Deadline d = new Deadline("submit", dt);
        assertEquals("submit", d.getTitle());
        assertFalse(d.getIsComplete());
        // Encoded string uses ISO_LOCAL_DATE_TIME
        assertTrue(d.toEncodedString().startsWith("D | 0 | submit | "));
        assertTrue(d.toString().contains("(By: "));
    }

    @Test
    public void testEventEncodingAndDisplay() {
        LocalDateTime from = LocalDateTime.of(2025, 1, 1, 14, 0);
        LocalDateTime to = LocalDateTime.of(2025, 1, 1, 16, 0);
        Event e = new Event("party", from, to);
        assertEquals("party", e.getTitle());
        assertFalse(e.getIsComplete());
        assertTrue(e.toEncodedString().startsWith("E | 0 | party | "));
        assertTrue(e.toString().contains("(From: "));
    }
}
