// The task class tracks the name of the task, and whether the class is complete. It does not track the order of the class
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

    public void markComplete() {
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
