public class ToDo extends Task {
    public ToDo(String title) { super(title); }

    // Create a To Do task and specify the completion status
    public ToDo(String title, Boolean isComplete) {
        super(title, isComplete);
    }

    @Override
    public String toString() {
        return "[T] " + super.toString();
    }
}