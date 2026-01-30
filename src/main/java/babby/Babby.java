package babby;

import babby.ui.Ui;
import babby.task.TaskList;
import babby.parser.Parser;
import babby.storage.Storage;

import java.util.Scanner;

import java.io.FileNotFoundException;

/**
 * Entry point and main application class for Babby.
 * I forgot to add the Javadoc branch to the earlier commit, so I used CoPilot to
 * generate the comments for this class
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
     * Creates a new Babby application with the given file path for persistence.
     *
     * @param filepath path to the tasks file
     */
    public Babby(String filepath) {
        this.storage = new Storage(filepath);
        this.ui = new Ui();
        this.taskList = new TaskList();
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
            if (!continueLoop) return;
        }
    }

    /**
     * Application entrypoint. Starts Babby with the default tasks file.
     *
     * @param args command-line arguments (ignored)
     */
    public static void main(String[] args) {
        new Babby("data/tasks.txt").run();
    }

    /**
     * Print the current tasks using the TaskList listing helper.
     */
    public void list() {
        this.taskList.list();
    }

    /**
     * Find tasks given the input using the TaskList listing helper.
     */
    public void find(String input) {
        this.taskList.find(input);
    }

    /**
     * Print the help message (delegates to Ui).
     */
    public void help() {
        this.ui.printHelp();
    }

    /**
     * Print goodbye message and perform any shutdown tasks (delegates to Ui).
     */
    public void bye() {
        this.ui.printGoodbye();
    }

    /**
     * Print a single logical output line via the Ui.
     *
     * @param message message to print
     */
    public void printLine(String message) {
        this.ui.printLine(message);
    }
}