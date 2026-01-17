// The task class tracks the name of the task, and whether the class is complete. The order of the tasks are tracked by Babby
public class Task {
    String title;
    Boolean isComplete;
    public Task(String title) {
        this.title = title;
        this.isComplete = false;
    }

    public void toggle() {
        this.isComplete = !this.isComplete;
    }

    public void markDone() {
        this.isComplete = true;
    }

    public void markToDo() {
        this.isComplete = false;
    }

    @Override
    public String toString() {
        String complete = this.isComplete ? "\u2714" : " ";
        return "[" + complete + "] " + title;
    }
}
