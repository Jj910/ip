// The task class tracks the name of the task, and whether the class is complete. The order of the tasks are tracked by Babby
public abstract class Task {
    private final String title;
    private Boolean isComplete;

    public Task(String title) {
        this.title = title;
        this.isComplete = false;
    }

    // Create a task and specify the completion status
    public Task(String title, Boolean isComplete) {
        this.title = title;
        this.isComplete = isComplete;
    }

    public void markDone() {
        this.isComplete = true;
    }

    public void markToDo() {
        this.isComplete = false;
    }

    public Boolean getIsComplete() {
        return this.isComplete;
    }

    public String getTitle() {
        return this.title;
    }

    /**
     * Encodes the task for it to be saved into the tasks file.
     *
     * @return Encoded string of the task.
     */
    public abstract String toEncodedString();

    @Override
    public String toString() {
        String complete = this.isComplete ? "✔" : " ";
        return "[" + complete + "] " + title;
    }
}