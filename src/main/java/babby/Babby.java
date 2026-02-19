package babby;

import java.io.FileNotFoundException;
import java.util.Scanner;

import babby.parser.Parser;
import babby.storage.Storage;
import babby.task.TaskList;
import babby.ui.Ui;

/**
 * Entry point and main application class for Babby.
 * Responsibilities:
 * - Wire together Storage, Ui and TaskList components.
 * - Run the main command loop and delegate parsing/execution to {@code Parser}.
 * This class intentionally keeps behavior minimal and exposes small helper
 * methods that {@code Parser} calls to perform actions or display output.
 */
public class Babby {
    private final Storage storage;
    private final Ui ui;
    private TaskList taskList;

    /**
     * Creates a new Babby application with the given file path for persistence.
     *
     * @param filepath path to the tasks file
     */
    public Babby(String filepath) {
        this.storage = new Storage(filepath);
        this.ui = new Ui();
        this.taskList = new TaskList();
    }

    public String getWelcomeMessage() {
        return this.ui.getWelcomeMessage();
    }

    public String getHelpMessage() {
        return this.ui.getHelpMessage();
    }

    /**
     * Returns the configured Storage component used for reading/writing tasks.
     *
     * @return the Storage instance
     */
    public Storage getStorage() {
        return this.storage;
    }

    /**
     * Returns the current in-memory TaskList.
     *
     * @return the TaskList used by the application
     */
    public TaskList getTaskList() {
        return this.taskList;
    }

    /**
     * Starts the application: loads tasks, prints the welcome message and
     * enters the main input loop. Parsing and command execution is delegated
     * to {@code Parser}.
     */
    public void run() {
        Scanner scanner = new Scanner(System.in); // Make scanner for user input
        Parser parser = new Parser(this);

        try {
            this.taskList = storage.parseTasks();
        } catch (FileNotFoundException e) {
            System.out.println("Error loading tasks: " + e.getMessage());
        }

        ui.printWelcomeMessage();

        // Main command loop
        while (true) {
            System.out.println("-------------------------------------");
            String input = scanner.nextLine();

            boolean continueLoop = parser.parseAndExecute(input);
            if (!continueLoop) {
                scanner.close();
                return;
            }
        }
    }

    /**
     * Starts Babby with the default tasks file. Is the main application entrypoint.
     *
     * @param args command-line arguments (ignored)
     */
    public static void main(String[] args) {
        try {
            new Babby("data/tasks.txt").run();
        } catch (Throwable t) {
            System.err.println("Unhandled exception in main:");
            t.printStackTrace();
            Throwable cause = t.getCause();
            while (cause != null) {
                System.err.println("Caused by:");
                cause.printStackTrace();
                cause = cause.getCause();
            }
            // ensure a non-zero exit code so CI notices the failure
            System.exit(1);
        }
    }

    /**
     * Generates a response for the user's chat message.
     */
    public String getResponse(String input) {
        Parser parser = new Parser(this);
        return parser.parseAndReturnOutput(input);
    }

    /**
     * Prints the current tasks using the TaskList listing helper.
     */
    public void list() {
        this.taskList.list();
    }

    /**
     * @return String representation of current tasks using the TaskList listing helper.
     */
    public String getListString() {
        return this.taskList.toString();
    }

    /**
     * Finds tasks given the input using the TaskList listing helper.
     */
    public void find(String input) {
        this.taskList.find(input);
    }

    /**
     * Prints the help message (delegates to Ui).
     */
    public void help() {
        this.ui.printHelpMessage();
    }

    /**
     * Prints goodbye message and perform any shutdown tasks (delegates to Ui).
     */
    public void byeCommand() {
        this.ui.printGoodbyeMessage();
    }

    /**
     * Prints a single logical output line via the Ui.
     *
     * @param message message to print
     */
    public void printLine(String message) {
        this.ui.printLine(message);
    }
}
