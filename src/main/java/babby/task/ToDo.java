package babby.task;

/**
 * Represents a To Do task with a title and completion status.
 * Inherits from the Task class.
 */
public class ToDo extends Task {
    /**
     * Creates a To Do task.
     * The task is marked as incomplete by default.
     *
     * @param title Title of the To Do task.
     */
    public ToDo(String title) {
        super(title);
    }

    /**
     * Creates a To Do task with specified completion status.
     *
     * @param title Title of the To Do task.
     * @param isComplete Completion status of the task. True if the task is complete, false otherwise.
     */
    public ToDo(String title, boolean isComplete) {
        super(title, isComplete);
    }

    /**
     * @inheritDoc
     *
     * @return Encoded string of the task in the format "T | {1/0} | title".
     */
    @Override
    public String toEncodedString() {
        return "T | " + (super.getIsComplete() ? "1" : "0") + " | " + super.getTitle();
    }

    @Override
    public String toString() {
        return "[T] " + super.toString();
    }
}
