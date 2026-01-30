/**
 * Tracks the name of the task, and whether the task is complete. The order of the tasks are tracked by Babby.
 */
public abstract class Task {
    private final String title;
    private Boolean isComplete;

    /**
     * Creates a task with the title specified.
     * The task is marked as incomplete by default.
     *
     * @param title Title of the task.
     */
    public Task(String title) {
        this.title = title;
        this.isComplete = false;
    }

    /**
     * Creates a task with specified completion status.
     * @param title Title of the task.
     * @param isComplete Completion status of the task. True if the task is complete, false otherwise.
     */
    public Task(String title, Boolean isComplete) {
        this.title = title;
        this.isComplete = isComplete;
    }

    /**
     * Encodes the task for it to be saved into the tasks file.
     *
     * @return Encoded string of the task in the format specific to the task type.
     */
    public abstract String toEncodedString();

    @Override
    public String toString() {
        String complete = this.isComplete ? "✔" : " ";
        return "[" + complete + "] " + title;
    }

    public Boolean getIsComplete() {
        return this.isComplete;
    }

    public String getTitle() {
        return this.title;
    }

    public void setDone() {
        this.isComplete = true;
    }

    public void setToDo() {
        this.isComplete = false;
    }
}