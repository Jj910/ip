public class Event extends Task {
    private final String from;
    private final String to;
    public Event(String title, String from, String to) {
        super(title);
        this.from = from;
        this.to = to;
    }

    // Create an event and specify the completion status
    public Event(String title, String from, String to, Boolean isComplete) {
        super(title, isComplete);
        this.from = from;
        this.to = to;
    }

    @Override
    public String toEncodedString() {
        return "E | " + (super.getIsComplete() ? "1" : "0") + " | " + super.getTitle() + " | "
                + this.from + " | " + this.to;
    }

    @Override
    public String toString() {
        return "[E] " + super.toString() + " (From: " + this.from + " To: " + this.to + ")";
    }
}