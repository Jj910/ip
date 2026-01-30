/**
 * Represents an Event task with a title, completion status, start date/time, and end date/time.
 * Inherits from the Task class.
 */
public class Event extends Task {
    private final String from;
    private final String to;

    /**
     * Creates an Event task.
     * The task is marked as incomplete by default.
     *
     * @param title Title of the event.
     * @param from Start time of the event.
     * @param to End time of the event.
     */
    public Event(String title, String from, String to) {
        super(title);
        this.from = from;
        this.to = to;
    }

    /**
     * Creates an Event task with specified completion status.
     *
     * @param title Title of the event.
     * @param from Start time of the event.
     * @param to End time of the event.
     * @param isComplete Completion status of the task. True if the task is complete, false otherwise.
     */
    public Event(String title, String from, String to, Boolean isComplete) {
        super(title, isComplete);
        this.from = from;
        this.to = to;
    }

    /**
     * @inheritDoc
     *
     * @return Encoded string of the task in the format "E | {1/0} | title | from | to".
     */
    @Override
    public String toEncodedString() {
        return "E | " + (super.getIsComplete() ? "1" : "0") + " | " + super.getTitle() + " | "
                + this.from + " | " + this.to;
    }

    @Override
    public String toString() {
        return "[E] " + super.toString() + " (From: " + this.from + " To: " + this.to + ")";
    }
}