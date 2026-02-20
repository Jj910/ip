package babby.task;

/**
 *  Tracks the friend contacts.
 */
public class Friend extends Task {
    private final int number;

    /**
     * Creates a contact with a specified name.
     *
     * @param name Name of the friend.
     */
    public Friend(String name, int number) {
        super(name);
        this.number = number;
    }

    public Friend(String name, int number, boolean isComplete) {
        super(name, isComplete);
        this.number = number;
    }

    /**
     * @inheritDoc
     *
     * @return Encoded string of the friend in the format .
     */
    @Override
    public String toEncodedString() {
        return "F | " + (super.getIsComplete() ? "1" : "0") + " | " + super.getTitle() + " | " + number;
    }

    @Override
    public String toString() {
        return "[F] " + super.toString() + " (Contact: " + number + ")";
    }
}
