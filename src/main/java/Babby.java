// My little helper, Babby
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import java.util.Scanner;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;

public class Babby {
    private Storage storage;
    private TaskList taskList;
    private Ui ui;

    private static final String FILEPATH = "data/tasks.txt";

    private static final DateTimeFormatter FILE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private static final DateTimeFormatter INPUT_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HHmm");

    public Babby() {
        this.storage = new Storage();
        this.ui = new Ui();
        this.taskList = new TaskList();
    }

    public void run() {
        Scanner scanner = new Scanner(System.in); // Make scanner
        File taskFile = initiateTaskFile();

        try {
            this.taskList = parseTasks(taskFile);
        } catch (FileNotFoundException e) {
            System.out.println("Error loading tasks: " + e.getMessage());
        }

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
        help();

        // Main command loop
        while (true) {
            System.out.println("-------------------------------------");
            String input = scanner.nextLine();

            // Command switch
            switch (Command.parse(input.split(" ")[0])) { // Add new commands to enum Command also
                case TODO -> todo(input);
                case DEADLINE -> deadline(input);
                case EVENT -> event(input);
                case LIST -> list();
                case MARK -> mark(input);
                case UNMARK -> unmark(input);
                case DELETE -> delete(input);
                case HELP -> help();
                case BYE -> {
                    System.out.println("\tByebyee! See you again soon!");
                    return;
                }
                default -> System.out.println("\tI'm sorry, I didn't quite get that :<\n\tCould you try again?");
            }
        }
    }

    public static void main(String[] args) {
        new Babby().run();
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
     * Initiates the tasks file. If the file does not exist, it creates a new one.
     *
     * @return File object representing the tasks file.
     */
    private File initiateTaskFile() {
        File tasks = new File(FILEPATH);

        try {
            System.out.println("Loading task file...");
            if (tasks.createNewFile()) {
                System.out.println("File not found! New task file created");
            } else {
                System.out.println("Task file loaded!");
            }
        } catch (IOException e) {
            System.out.println("Error opening/creating tasks file: " + e.getMessage());
        }
        return tasks;
    }

    /**
     * Saves all tasks.
     **/
    private void saveTasks() {
        try {
            FileWriter fw = new FileWriter(FILEPATH);
            System.out.println(1);
            for (Task task : taskList) {
                System.out.println(task);
                fw.write(task.toEncodedString() + System.lineSeparator());
            }
            fw.close();
        } catch (IOException e) {
            System.out.println("Error saving task: " + e.getMessage());
        }
    }

    /**
     * Returns a populated ArrayList with all tasks from the given file.
     *
     * @param tasks File object for task file.
     * @throws FileNotFoundException If given file is not found.
     */
     private TaskList parseTasks(File tasks) throws FileNotFoundException {
        System.out.println("Hold on... I'm reading the tasks...");
         TaskList taskList = new TaskList();
        Scanner s = new Scanner(tasks);
        while (s.hasNextLine()) {
            String nextLine = s.nextLine();
            String[] taskLine = nextLine.split(" \\| ");
            String taskType = taskLine[0];
            String taskTitle = taskLine[2];
            Boolean isComplete = taskLine[1].equals("1");
            // Parse for each type of task then add to the task list
            try {
                switch (taskType) {
                    case "T" ->  taskList.add(new ToDo(taskTitle, isComplete)); // To Do
                    case "D" ->  {
                        LocalDateTime by = LocalDateTime.parse(taskLine[3], FILE_FORMATTER);
                        taskList.add(new Deadline(taskTitle, by, isComplete)); // Deadline
                    }
                    case "E" ->  {
                        LocalDateTime from = LocalDateTime.parse(taskLine[3], FILE_FORMATTER);
                        LocalDateTime to = LocalDateTime.parse(taskLine[4], FILE_FORMATTER);
                        taskList.add(new Event(taskTitle, from, to, isComplete)); // Event
                    }
                    default -> System.out.println("I can't read this task:\n\t\"" + nextLine + "\"\nSkipping it...");
                }
            } catch (Exception e) {
                System.out.println("I can't read this task:\n\t\"" + nextLine + "\"\nSkipping it...");
            }
        }
        System.out.println("Tasks loaded!");
        return taskList;
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
            System.out.println("\tOopsie! The description of a task cannot be empty :<");
            return;
        }
        ToDo task = new ToDo(inputList[1]);
        taskList.add(task);
        saveTasks();
        System.out.println("\tOkay, I added this task: " + task);
        System.out.println("\tYou have " + taskList.size() + " tasks in the list now!");
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
            System.out.println("\tOopsie! You didn't follow the command's format! :<\n\tTry something like \"deadline meet friends /by 31/12/2025 2359\"");
            return;
        }
        try {
            LocalDateTime by = LocalDateTime.parse(inputList[1], INPUT_FORMATTER);
            Deadline task = new Deadline(inputList[0], by);
            taskList.add(task);
            saveTasks();
            System.out.println("\tOkay, I added this task: " + task);
            System.out.println("\tYou have " + taskList.size() + " tasks in the list now!");
        } catch (DateTimeParseException e) {
            System.out.println("\tOopsie! The date/time you provided is wrong. Try something like 31/12/2025 2359");
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
            System.out.println("\tOopsie! You didn't follow the command's format! :<" +
                    "\n\tTry something like \"meet friends /from 01/01/2025 1400 /to 01/01/2025 1600\"");
            return;
        }
        try {
            LocalDateTime from = LocalDateTime.parse(inputList[1], INPUT_FORMATTER);
            LocalDateTime to = LocalDateTime.parse(inputList[2], INPUT_FORMATTER);
            Event task = new Event(inputList[0], from, to);
            taskList.add(task);
            saveTasks();
            System.out.println("\tOkay, I added this task: " + task);
            System.out.println("\tYou have " + taskList.size() + " tasks in the list now!");
        } catch (DateTimeParseException e) {
            System.out.println("\tOopsie! The date/time you provided is wrong. Please use DD/MM/YYYY HHMM");
        }
    }

    /**
     * Lists all tasks in the task list.
     */
    public void list() {
        if (taskList.isEmpty()) {
            System.out.println("\tYour task list is empty! Add some tasks first :)");
            return;
        }

        int i = 1;
        System.out.println("\tHere are your tasks:");
        for (Task task : taskList) {
            System.out.println("\t\t" + i + ") " + task);
            i++;
        }
    }

    /**
     * Marks the given task as done.
     *
     * @param input User input string in the format "mark {task number}".
     */
    public void mark(String input) {
        String[] inputList = input.split(" ");
        int index = Integer.parseInt(inputList[1]) - 1;
        Task task = taskList.get(index);
        task.setDone(); // Mark the task as done
        saveTasks();
        System.out.println("\tGood job! You completed this task:\n\t\t" + task);
    }

    /**
     * Marks the given task as incomplete.
     *
     * @param input User input string in the format "unmark {task number}".
     */
    public void unmark(String input) {
        String[] inputList = input.split(" ");
        int index = Integer.parseInt(inputList[1]) - 1;
        Task task = taskList.get(index);
        task.setToDo(); // Mark the task as not done
        saveTasks();
        System.out.println("\tOkay, you need to do this task:\n\t\t" + task);
    }

    /**
     * Deletes the given task from the task list.
     *
     * @param input User input string in the format "delete {task number}".
     */
    public void delete(String input) {
        String[] inputList = input.split(" ");
        int index = Integer.parseInt(inputList[1]) - 1;
        Task task = taskList.remove(index);
        saveTasks();
        System.out.println("\tOkies, I deleted this task:" + task);
        System.out.println("\tYou have " + taskList.size() + " tasks in the list now!");
    }

    /**
     * Prints help message listing all commands.
     */
    public static void help() {
        System.out.println("\tToDo {task} -> Adds a todo task");
        System.out.println("\tDeadline {task} /by {DD/MM/YYYY HHMM} -> Adds a deadline task");
        System.out.println("\tEvent {task} /from {DD/MM/YYYY HHMM} /to {DD/MM/YYYY HHMM} -> Adds a event task\n");
        System.out.println("\tList -> Lists all tasks");
        System.out.println("\tMark {task number} -> Marks the task as done");
        System.out.println("\tUnmark {task number} -> Marks the task as not done");
        System.out.println("\tDelete {task number} -> Deletes the task from the list\n");
        System.out.println("\tHelp -> Shows this help message");
        System.out.println("\tBye -> Exits the program :<\n");
    }
}