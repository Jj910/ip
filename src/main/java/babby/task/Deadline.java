package babby.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a Deadline task with a title, completion status, and due date/time.
 * Inherits from the Task class.
 */
public class Deadline extends Task {
    private static final DateTimeFormatter FILE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private static final DateTimeFormatter DISPLAY_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HHmm");

    private final LocalDateTime by;

    /**
     * Creates a Deadline task.
     * The task is marked as incomplete by default.
     *
     * @param title Title of the Deadline task.
     * @param by Deadline of the task.
     */
    public Deadline(String title, LocalDateTime by) {
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
    public Deadline(String title, LocalDateTime by, boolean isComplete) {
        super(title, isComplete);
        this.by = by;
    }

    /**
     * @inheritDoc
     *
     * @return Encoded string of the task in the format "D | {1/0} | title | by" where "by" is saved in ISO format.
     */
    @Override
    public String toEncodedString() {
        return "D | " + (super.getIsComplete() ? "1" : "0") + " | " + super.getTitle()
                + " | " + this.by.format(FILE_FORMATTER);
    }

    @Override
    public String toString() {
        return "[D] " + super.toString() + " (By: " + this.by.format(DISPLAY_FORMATTER) + ")";
    }
}
