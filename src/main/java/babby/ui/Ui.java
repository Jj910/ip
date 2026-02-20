package babby.ui;

/**
 * This class represents the User Interface component of the application.
 * It is responsible for rendering the UI elements and handling user interactions.
 */
public class Ui {
    public void printLine(String message) {
        System.out.println("\t" + message);
    }

    /**
     * Prints the welcome message along with the ASCII art logo.
     */
    public void printWelcomeMessage() {
        this.getWelcomeMessage();
    }
    /**
     * Returns the welcome message string with ASCII art logo.
     *
     * @return Welcome message string.
     */
    public String getWelcomeMessage() {
        String logo = """
                 ______        _     _           _\s
                (____  \\      | |   | |         | |\

                 ____)  )_____| |__ | |__  _   _| |
                |  __  ((____ |  _ \\|  _ \\| | | |_|\

                | |__)  ) ___ | |_) ) |_) ) |_| |_\s
                |______/\\_____|____/|____/ \\__  |_|\

                                          (____/  \s""";
        return "Hello! I'm Babby!\nSo nice to meet you! Lets be friends <3"
                + "\n----------------------------------\n"
                + this.getHelpMessage();
    }

    public void printGoodbyeMessage() {
        this.printLine("Byebyee! See you again soon!");
    }

    /**
     * Prints help message listing all commands.
     */
    public void printHelpMessage() {
        printLine(getHelpMessage());
    }

    /**
     * Returns help message listing all commands.
     *
     * @return Help message string.
     */
    public String getHelpMessage() {
        return """
                Here are the commands you can use:
                ToDo {task} -> Adds a todo task
                Deadline {task} /by {DD/MM/YYYY HHMM} -> Adds a deadline task
                Event {task} /from {DD/MM/YYYY HHMM} /to {DD/MM/YYYY HHMM} -> Adds a event task
                Friend {name} /number {phone number} -> Adds a friend to your friend list
                \n
                List -> Lists all tasks
                Find {text} -> Finds all tasks with your input, you can search for date/time too!
                Mark {task number} -> Marks the task as done
                Unmark {task number} -> Marks the task as not done
                Delete {task number} -> Deletes the task from the list
                \n
                Help -> Shows this help message
                Bye -> Exits the program :<
                
                """;
    }
}
