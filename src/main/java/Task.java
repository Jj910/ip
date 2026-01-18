// The task class tracks the name of the task, and whether the class is complete. The order of the tasks are tracked by Babby
public abstract class Task {
    private final String title;
    private Boolean isComplete;

    public Task(String title) {
        this.title = title;
        this.isComplete = false;
    }

    public void markDone() {
        this.isComplete = true;
    }

    public void markToDo() {
        this.isComplete = false;
    }

    @Override
    public String toString() {
        String complete = this.isComplete ? "✔" : " ";
        return "[" + complete + "] " + title;
    }
}