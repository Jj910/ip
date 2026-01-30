// My little helper, Babby

import java.util.Scanner;

import java.io.FileNotFoundException;

public class Babby {
    private final Storage storage;
    private final Ui ui;
    private TaskList taskList;


    // Expose storage and tasklist for Parser
    public Storage getStorage() {
        return this.storage;
    }

    public TaskList getTaskList() {
        return this.taskList;
    }


    public Babby(String filepath) {
        this.storage = new Storage(filepath);
        this.ui = new Ui();
        this.taskList = new TaskList();
    }

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

    public static void main(String[] args) {
        new Babby("data/tasks.txt").run();
    }

    // Methods called by Parser
    public void list() {
        this.taskList.list();
    }

    public void help() {
        this.ui.printHelp();
    }

    public void bye() {
        this.ui.printGoodbye();
    }

    public void printLine(String message) {
        this.ui.printLine(message);
    }
}