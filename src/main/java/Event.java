import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents an Event task with a title, completion status, start date/time, and end date/time.
 * Inherits from the Task class.
 */
public class Event extends Task {
    private final LocalDateTime from;
    private final LocalDateTime to;

    private static final DateTimeFormatter FILE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private static final DateTimeFormatter DISPLAY_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HHmm");

    /**
     * Creates an Event task.
     * The task is marked as incomplete by default.
     *
     * @param title Title of the event.
     * @param from Start time of the event.
     * @param to End time of the event.
     */
    public Event(String title, LocalDateTime from, LocalDateTime to) {
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
    public Event(String title, LocalDateTime from, LocalDateTime to, Boolean isComplete) {
        super(title, isComplete);
        this.from = from;
        this.to = to;
    }

    /**
     * @inheritDoc
     *
     * @return Encoded string of the task in the format "E | {1/0} | title | from | to" where datetimes are saved in ISO format.
     */
    @Override
    public String toEncodedString() {
        return "E | " + (super.getIsComplete() ? "1" : "0") + " | " + super.getTitle() + " | "
                + this.from.format(FILE_FORMATTER) + " | " + this.to.format(FILE_FORMATTER);
    }

    @Override
    public String toString() {
        return "[E] " + super.toString() + " (From: " + this.from.format(DISPLAY_FORMATTER) + " To: " + this.to.format(DISPLAY_FORMATTER) + ")";
    }
}