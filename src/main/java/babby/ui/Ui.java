package babby.ui;

/**
 * This class represents the User Interface component of the application.
 * It is responsible for rendering the UI elements and handling user interactions.
 */
public class Ui {
    public void printLine(String message) {
        System.out.println("\t" + message);
    }
    
    public void printWelcomeMessage() {
        String logo = """
                 ______        _     _           _\s
                (____  \\      | |   | |         | |\

                 ____)  )_____| |__ | |__  _   _| |
                |  __  ((____ |  _ \\|  _ \\| | | |_|\

                | |__)  ) ___ | |_) ) |_) ) |_| |_\s
                |______/\\_____|____/|____/ \\__  |_|\

                                          (____/  \s""";
        System.out.println("Hello! I'm\n" + logo +"\nSo nice to meet you! Lets be friends <3" +
                "\n----------------------------------\n");
        System.out.println("What can I do for you?");
        this.printHelp();
    }

    public void printGoodbye() {
        this.printLine("Byebyee! See you again soon!");
    }

    /**
     * Prints help message listing all commands.
     */
    public void printHelp() {
        this.printLine("ToDo {task} -> Adds a todo task");
        this.printLine("Deadline {task} /by {DD/MM/YYYY HHMM} -> Adds a deadline task");
        this.printLine("Event {task} /from {DD/MM/YYYY HHMM} /to {DD/MM/YYYY HHMM} -> Adds a event task\n");
        this.printLine("List -> Lists all tasks");
        this.printLine("Mark {task number} -> Marks the task as done");
        this.printLine("Unmark {task number} -> Marks the task as not done");
        this.printLine("Delete {task number} -> Deletes the task from the list\n");
        this.printLine("Help -> Shows this help message");
        this.printLine("Bye -> Exits the program :<\n");
    }
}
