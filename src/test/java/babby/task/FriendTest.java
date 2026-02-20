package babby.task;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class FriendTest {
    @Test
    public void toEncodedStringincludesFriendPrefixAndNumber() {
        Friend friend = new Friend("Alice", 12345678);
        assertEquals("F | 0 | Alice | 12345678", friend.toEncodedString());
    }

    @Test
    public void toStringincludesStatusAndPhoneNumber() {
        Friend friend = new Friend("Bob", 999, true);
        assertEquals("[F] [✔] Bob (Contact: 999)", friend.toString());
    }
}
