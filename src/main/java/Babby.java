// My little helper, Babby
import java.io.FileNotFoundException;

import java.util.*;
import java.util.Scanner;

// Import for files
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Babby {
    // List to store user input
    private static ArrayList<Task> taskList = new ArrayList<>();
    private static final String FILEPATH = "data/tasks.txt";

    // Command enums
    private enum Command {
        TODO("todo"),
        DEADLINE("deadline"),
        EVENT("event"),
        LIST("list"),
        MARK("mark"),
        UNMARK("unmark"),
        DELETE("delete"),
        BYE("bye"),
        UNKNOWN("");

        private final String command;

        Command(String command) {
            this.command = command;
        }

        public static Command parse(String input) {
            if (input == null || input.isEmpty()) return UNKNOWN;
            for (Command command : values()) {
                if (input.equals(command.command)) return command;
            }
            return UNKNOWN;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in); // Make scanner
        File taskFile = initiateTaskFile();

        try {
            System.out.println("Loading tasks...");
            taskList = parseTasks(taskFile);
        } catch (FileNotFoundException e) {
            System.out.println("Error loading tasks: " + e.getMessage());
        }

        String logo = " ______        _     _           _ \n(____  \\      | |   | |         | |" +
                "\n ____)  )_____| |__ | |__  _   _| |\n|  __  ((____ |  _ \\|  _ \\| | | |_|" +
                "\n| |__)  ) ___ | |_) ) |_) ) |_| |_ \n|______/\\_____|____/|____/ \\__  |_|" +
                "\n                          (____/   ";
        System.out.println("Hello! I'm\n" + logo +"\nSo nice to meet you! Lets be friends <3" +
                "\n----------------------------------\n");
        System.out.println("What can I do for you?");
        System.out.println("\ttodo {task} -> Adds a todo task");
        System.out.println("\tdeadline {task} /by {deadline} -> Adds a deadline task");
        System.out.println("\tevent {task} /from {start time} /to {end time} -> Adds a event task\n");
        System.out.println("\tlist -> Lists all tasks");
        System.out.println("\tmark {task number} -> Marks the task as done");
        System.out.println("\tunmark {task number} -> Marks the task as not done");
        System.out.println("\tdelete {task number} -> Deletes the task from the list\n");
        System.out.println("\tbye -> Exits the program :<\n");

        // Main command loop
        while (true) {
            System.out.println("-------------------------------------");
            String input = scanner.nextLine();

            // Command switch
            switch (Command.parse(input.split(" ")[0])) {
                case TODO -> todo(input);
                case DEADLINE -> deadline(input);
                case EVENT -> event(input);
                case LIST -> list();
                case MARK -> mark(input);
                case UNMARK -> unmark(input);
                case DELETE -> delete(input);
                case BYE -> {
                    System.out.println("\tByebyee! See you again soon!");
                    return;
                }
                default -> System.out.println("\tI'm sorry, I didn't quite get that :<\n\tCould you try again?");
            }
        }
    }

    /**
     * Initiates the tasks file. If the file does not exist, it creates a new one.
     *
     * @return File object representing the tasks file.
     */
    public static File initiateTaskFile() {
        File tasks = new File(FILEPATH);

        try {
            System.out.println("Loading task file...");
            if (tasks.createNewFile()) {
                System.out.println("File not found! New task file created");
            }
        } catch (IOException e) {
            System.out.println("Error opening/creating tasks file: " + e.getMessage());
        }

        return tasks;
    }

    /**
     * Saves all tasks.
     **/
    public static void saveTasks() {
        try {
            FileWriter fw = new FileWriter(FILEPATH);
            System.out.println(1);
            for (Task task : taskList) {
                System.out.println(task);
                fw.write(task.toEncodedString() + System.lineSeparator());
            };
            fw.close();
        } catch (IOException e) {
            System.out.println("Error saving task: " + e.getMessage());
        }
    }

    /**
     * Returns a populated ArrayList with all tasks from the given file.
     *
     * @param tasks X coordinate of position.
     * @throws FileNotFoundException If given file is not found.
     */
     private static ArrayList<Task> parseTasks(File tasks) throws FileNotFoundException {
        ArrayList<Task> taskList = new ArrayList<>();
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
                    case "D" ->  taskList.add(new Deadline(taskTitle, taskLine[3], isComplete)); // Deadline
                    case "E" ->  taskList.add(new Event(taskTitle, taskLine[3], taskLine[4], isComplete)); // Event
                    default -> System.out.println("I can't read this task:\n\t\"" + nextLine + "\"\nSkipping it...");
                }
            } catch (Exception e) {
                System.out.println("I can't read this task:\n\t\"" + nextLine + "\"\nSkipping it...");
            }
        }
        return taskList;
    }

    public static void todo(String input) {
        String[] inputList = input.split("todo ");
        ToDo task = new ToDo(inputList[1]);
        taskList.add(task);
        saveTasks();
        System.out.println("\tOkay, I added this task: " + task);
        System.out.println("\tYou have " + taskList.size() + " tasks in the list now!");
    }

    public static void deadline(String input) {
        String[] inputList = input.replaceFirst("deadline ", "").split(" /by ");
        Deadline task = new Deadline(inputList[0], inputList[1]);
        taskList.add(task);
        saveTasks();
        System.out.println("\tOkay, I added this task: " + task);
        System.out.println("\tYou have " + taskList.size() + " tasks in the list now!");
    }

    public static void event(String input) {
        String[] inputList = input.replaceFirst("event ", "").split(" /from | /to ");
        Event task = new Event(inputList[0], inputList[1], inputList[2]);
        taskList.add(task);
        saveTasks();
        System.out.println("\tOkay, I added this task: " + task);
        System.out.println("\tYou have " + taskList.size() + " tasks in the list now!");
    }

    public static void list() {
        int i = 1;
        System.out.println("\tHere are your tasks:");
        for (Task task : taskList) {
            System.out.println("\t\t" + i + ") " + task);
            i++;
        }
    }

    public static void mark(String input) {
        String[] inputList = input.split(" ");
        int index = Integer.parseInt(inputList[1]) - 1;
        Task task = taskList.get(index);
        task.setDone(); // Mark the task as done
        saveTasks();
        System.out.println("\tGood job! You completed this task:\n\t\t" + task);
    }

    public static void unmark(String input) {
        String[] inputList = input.split(" ");
        int index = Integer.parseInt(inputList[1]) - 1;
        Task task = taskList.get(index);
        task.setToDo(); // Mark the task as not done
        saveTasks();
        System.out.println("\tOkay, you need to do this task:\n\t\t" + task);
    }

    public static void delete(String input) {
        String[] inputList = input.split(" ");
        int index = Integer.parseInt(inputList[1]) - 1;
        Task task = taskList.remove(index);
        saveTasks();
        System.out.println("\tOkies, I deleted this task:" + task);
        System.out.println("\tYou have " + taskList.size() + " tasks in the list now!");
    }
}