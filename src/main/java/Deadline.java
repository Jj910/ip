public class Deadline extends Task {
    private final String by;
    public Deadline(String title, String by) {
        super(title);
        this.by = by;
    }

    // Create a deadline and specify the completion status
    public Deadline(String title, String by, Boolean isComplete) {
        super(title, isComplete);
        this.by = by;
    }

    @Override
    public String toEncodedString() {
        return "D | " + (super.getIsComplete() ? "1" : "0") + " | " + super.getTitle() + " | " + this.by;
    }

    @Override
    public String toString() {
        return "[D] " + super.toString() + " (By: " + this.by + ")";
    }
}