// My little helper, Babby
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import java.util.Scanner;

import java.io.FileNotFoundException;

public class Babby {
    private Storage storage;
    private TaskList taskList;
    private Ui ui;

    private static final DateTimeFormatter INPUT_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HHmm");


    public Babby(String filepath) {
        this.storage = new Storage(filepath);
        this.ui = new Ui();
        this.taskList = new TaskList();
    }

    public void run() {
        Scanner scanner = new Scanner(System.in); // Make scanner for user input

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

            // Command switch
            switch (Command.parse(input.split(" ")[0])) { // Add new commands to enum Command also
                case TODO -> todo(input);
                case DEADLINE -> deadline(input);
                case EVENT -> event(input);
                case LIST -> this.taskList.list();
                case MARK -> mark(input);
                case UNMARK -> unmark(input);
                case DELETE -> delete(input);
                case HELP -> ui.printHelp();
                case BYE -> {
                    ui.printGoodbye();
                    return;
                }
                default -> ui.printLine("I'm sorry, I didn't quite get that :<\n\tCould you try again?");
            }
        }
    }

    public static void main(String[] args) {
        new Babby("data/tasks.txt").run();
    }

    // Command enums
    private enum Command {
        TODO("todo"),
        DEADLINE("deadline"),
        EVENT("event"),
        LIST("list"),
        MARK("mark"),
        UNMARK("unmark"),
        DELETE("delete"),
        HELP("help"),
        BYE("bye"),
        UNKNOWN("");

        private final String command;

        Command(String command) {
            this.command = command;
        }

        public static Command parse(String input) {
            if (input == null || input.isEmpty()) return UNKNOWN;
            for (Command command : values()) {
                if (input.toLowerCase().equals(command.command)) return command;
            }
            return UNKNOWN;
        }
    }



    /**
     * Adds a To Do task to the task list.
     * A To Do task is a task without any date/time attached to it.
     *
     * @param input User input string in the format "todo {task}".
     */
    public void todo(String input) {
        String[] inputList = input.split("todo ");
        if (inputList.length < 2 || inputList[1].isBlank()) {
            ui.printLine("Oopsie! The description of a task cannot be empty :<");
            return;
        }
        ToDo task = new ToDo(inputList[1]);
        this.taskList.add(task);
        storage.saveTasks(this.taskList);
        ui.printLine("Okay, I added this task: " + task);
        ui.printLine("You have " + this.taskList.size() + " tasks in the list now!");
    }

    /**
     * Adds a Deadline task to the task list.
     * A Deadline task is a task that needs to be done by a specific date/time.
     *
     * @param input User input string in the format "deadline {task} /by {deadline}".
     */
    public void deadline(String input) {
        String[] inputList = input.replaceFirst("deadline ", "").split(" /by ");
        if (inputList.length < 2 || inputList[0].isBlank() || inputList[1].isBlank()) {
            ui.printLine("Oopsie! You didn't follow the command's format! :<");
            ui.printLine("Try something like \"deadline meet friends /by 31/12/2025 2359\"");
            return;
        }
        try {
            LocalDateTime by = LocalDateTime.parse(inputList[1], INPUT_FORMATTER);
            Deadline task = new Deadline(inputList[0], by);
            this.taskList.add(task);
            storage.saveTasks(this.taskList);
            ui.printLine("Okay, I added this task: " + task);
            ui.printLine("You have " + this.taskList.size() + " tasks in the list now!");
        } catch (DateTimeParseException e) {
            ui.printLine("Oopsie! The date/time you provided is wrong. Try something like 31/12/2025 2359");
        }
    }

    /**
     * Adds an Event task to the task list.
     * An Event task is a task that starts at a specific time and ends at a specific time.
     *
     * @param input User input string in the format "event {task} /from {start time} /to {end time}".
     */
    public void event(String input) {
        String[] inputList = input.replaceFirst("event ", "").split(" /from | /to ");
        if (inputList.length < 3 || inputList[0].isBlank() || inputList[1].isBlank() || inputList[2].isBlank()) {
            ui.printLine("Oopsie! You didn't follow the command's format! :<");
            ui.printLine("Try something like \"meet friends /from 01/01/2025 1400 /to 01/01/2025 1600\"");
            return;
        }
        try {
            LocalDateTime from = LocalDateTime.parse(inputList[1], INPUT_FORMATTER);
            LocalDateTime to = LocalDateTime.parse(inputList[2], INPUT_FORMATTER);
            Event task = new Event(inputList[0], from, to);
            this.taskList.add(task);
            storage.saveTasks(this.taskList);
            ui.printLine("Okay, I added this task: " + task);
            ui.printLine("You have " + this.taskList.size() + " tasks in the list now!");
        } catch (DateTimeParseException e) {
            ui.printLine("Oopsie! The date/time you provided is wrong. Please use DD/MM/YYYY HHMM");
        }
    }

    /**
     * Marks the given task as done.
     *
     * @param input User input string in the format "mark {task number}".
     */
    public void mark(String input) {
        String[] inputList = input.split(" ");
        // Validate that a task number is provided
        if (inputList.length < 2) {
            ui.printLine("Oopsie! You didn't provide a task number to mark! :<");
            return;
        }
        // Validate that the task number is an integer
        if (!inputList[1].matches("\\d+")) {
            ui.printLine("Oopsie! The task number must be a positive integer! :<");
            return;
        }
        // Validate that the task number is within the range of the task list
        int taskNumber = Integer.parseInt(inputList[1]);
        if (taskNumber < 1 || taskNumber > this.taskList.size()) {
            ui.printLine("Oopsie! The task number " + taskNumber + " does not exist! :<");
            return;
        }

        int index = Integer.parseInt(inputList[1]) - 1;
        Task task = this.taskList.get(index);
        task.setDone(); // Mark the task as done
        storage.saveTasks(this.taskList);
        ui.printLine("Good job! You completed this task:\n\t\t" + task);
    }

    /**
     * Marks the given task as incomplete.
     *
     * @param input User input string in the format "unmark {task number}".
     */
    public void unmark(String input) {
        String[] inputList = input.split(" ");
        // Validate that a task number is provided
        if (inputList.length < 2) {
            ui.printLine("Oopsie! You didn't provide a task number to unmark! :<");
            return;
        }
        // Validate that the task number is an integer
        if (!inputList[1].matches("\\d+")) {
            ui.printLine("Oopsie! The task number must be a positive integer! :<");
            return;
        }
        // Validate that the task number is within the range of the task list
        int taskNumber = Integer.parseInt(inputList[1]);
        if (taskNumber < 1 || taskNumber > this.taskList.size()) {
            ui.printLine("Oopsie! The task number " + taskNumber + " does not exist! :<");
            return;
        }

        int index = Integer.parseInt(inputList[1]) - 1;
        Task task = this.taskList.get(index);
        task.setToDo(); // Mark the task as not done
        storage.saveTasks(this.taskList);
        ui.printLine("Okay, you need to do this task:\n\t\t" + task);
    }

    /**
     * Deletes the given task from the task list.
     *
     * @param input User input string in the format "delete {task number}".
     */
    public void delete(String input) {
        String[] inputList = input.split(" ");
        // Validate that a task number is provided
        if (inputList.length < 2) {
            ui.printLine("Oopsie! You didn't provide a task number to unmark! :<");
            return;
        }
        // Validate that the task number is an integer
        if (!inputList[1].matches("\\d+")) {
            ui.printLine("Oopsie! The task number must be a positive integer! :<");
            return;
        }
        // Validate that the task number is within the range of the task list
        int taskNumber = Integer.parseInt(inputList[1]);
        if (taskNumber < 1 || taskNumber > this.taskList.size()) {
            ui.printLine("Oopsie! The task number " + taskNumber + " does not exist! :<");
            return;
        }

        int index = Integer.parseInt(inputList[1]) - 1;
        Task task = this.taskList.remove(index);
        storage.saveTasks(this.taskList);
        ui.printLine("Okies, I deleted this task:" + task);
        ui.printLine("You have " + taskList.size() + " tasks in the list now!");
    }
}