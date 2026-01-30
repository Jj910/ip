/**
 * Represents a Deadline task with a title, completion status, and due date/time.
 * Inherits from the Task class.
 */
public class Deadline extends Task {
    private final String by;

    /**
     * Creates a Deadline task.
     * The task is marked as incomplete by default.
     *
     * @param title Title of the Deadline task.
     * @param by Deadline of the task.
     */
    public Deadline(String title, String by) {
        super(title);
        this.by = by;
    }

    /**
     * Creates a Deadline task with specified completion status.
     *
     * @param title Title of the Deadline task.
     * @param by Deadline of the task.
     * @param isComplete Completion status of the task. True if the task is complete, false otherwise.
     */
    public Deadline(String title, String by, Boolean isComplete) {
        super(title, isComplete);
        this.by = by;
    }

    /**
     * @inheritDoc
     *
     * @return Encoded string of the task in the format "D | {1/0} | title | by".
     */
    @Override
    public String toEncodedString() {
        return "D | " + (super.getIsComplete() ? "1" : "0") + " | " + super.getTitle() + " | " + this.by;
    }

    @Override
    public String toString() {
        return "[D] " + super.toString() + " (By: " + this.by + ")";
    }
}