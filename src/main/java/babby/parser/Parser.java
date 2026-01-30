package babby.parser;

import babby.task.Task;
import babby.task.ToDo;
import babby.task.Deadline;
import babby.task.Event;
import babby.Babby;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Class that parses input data and calls the right commands.
 */
public class Parser {
    private final Babby babby;

    private static final DateTimeFormatter INPUT_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HHmm");

    public Parser(Babby babby) {
        this.babby = babby;
    }

    /**
     * Parse the input and execute the corresponding command on the application.
     *
     * @param input raw user input line
     * @return true to continue the main loop, false to exit (on bye)
     */
    public boolean parseAndExecute(String input) {
        String cmdToken = "";
        if (input != null && !input.isBlank()) {
            String[] parts = input.split(" ");
            if (parts.length > 0) cmdToken = parts[0];
        }

        // Normalize input to non-null to avoid passing null into methods
        String safeInput = (input == null) ? "" : input;

        // Add new commands to the command enums too
        switch (Command.parse(cmdToken)) {
            case TODO -> todo(safeInput);
            case DEADLINE -> deadline(safeInput);
            case EVENT -> event(safeInput);
            case LIST -> babby.list();
            case FIND -> find(safeInput);
            case MARK -> mark(safeInput);
            case UNMARK -> unmark(safeInput);
            case DELETE -> delete(safeInput);
            case HELP -> babby.help();
            case BYE -> {
                babby.bye();
                return false;
            }
            default -> babby.printLine("I'm sorry, I didn't quite get that :<\n\tCould you try again?");
        }
        return true;
    }

    // --- Command implementations ---
    private void todo(String input) {
        String[] inputList = input.split("todo ");
        if (inputList.length < 2 || inputList[1].isBlank()) {
            babby.printLine("Oopsie! The description of a task cannot be empty :<");
            return;
        }
        ToDo task = new ToDo(inputList[1]);
        babby.getTaskList().add(task);
        babby.getStorage().saveTasks(babby.getTaskList());
        babby.printLine("Okay, I added this task: " + task);
        babby.printLine("You have " + babby.getTaskList().size() + " tasks in the list now!");
    }

    private void deadline(String input) {
        String[] inputList = input.replaceFirst("deadline ", "").split(" /by ");
        if (inputList.length < 2 || inputList[0].isBlank() || inputList[1].isBlank()) {
            babby.printLine("Oopsie! You didn't follow the command's format! :<");
            babby.printLine("Try something like \"deadline meet friends /by 31/12/2025 2359\"");
            return;
        }
        try {
            LocalDateTime by = LocalDateTime.parse(inputList[1], INPUT_FORMATTER);
            Deadline task = new Deadline(inputList[0], by);
            babby.getTaskList().add(task);
            babby.getStorage().saveTasks(babby.getTaskList());
            babby.printLine("Okay, I added this task: " + task);
            babby.printLine("You have " + babby.getTaskList().size() + " tasks in the list now!");
        } catch (DateTimeParseException e) {
            babby.printLine("Oopsie! The date/time you provided is wrong. Try something like 31/12/2025 2359");
        }
    }

    private void event(String input) {
        String[] inputList = input.replaceFirst("event ", "").split(" /from | /to ");
        if (inputList.length < 3 || inputList[0].isBlank() || inputList[1].isBlank() || inputList[2].isBlank()) {
            babby.printLine("Oopsie! You didn't follow the command's format! :<");
            babby.printLine("Try something like \"meet friends /from 01/01/2025 1400 /to 01/01/2025 1600\"");
            return;
        }
        try {
            LocalDateTime from = LocalDateTime.parse(inputList[1], INPUT_FORMATTER);
            LocalDateTime to = LocalDateTime.parse(inputList[2], INPUT_FORMATTER);
            Event task = new Event(inputList[0], from, to);
            babby.getTaskList().add(task);
            babby.getStorage().saveTasks(babby.getTaskList());
            babby.printLine("Okay, I added this task: " + task);
            babby.printLine("You have " + babby.getTaskList().size() + " tasks in the list now!");
        } catch (DateTimeParseException e) {
            babby.printLine("Oopsie! The date/time you provided is wrong. Please use DD/MM/YYYY HHMM");
        }
    }

    private void mark(String input) {
        String[] inputList = input.split(" ");
        if (inputList.length < 2) {
            babby.printLine("Oopsie! You didn't provide a task number! :<");
            return;
        }
        if (!inputList[1].matches("\\d+")) {
            babby.printLine("Oopsie! The task number must be a positive integer! :<");
            return;
        }
        int taskNumber = Integer.parseInt(inputList[1]);
        if (taskNumber < 1 || taskNumber > babby.getTaskList().size()) {
            babby.printLine("Oopsie! The task number " + taskNumber + " does not exist! :<");
            return;
        }
        int index = taskNumber - 1;
        Task task = babby.getTaskList().get(index);
        task.setDone();
        babby.getStorage().saveTasks(babby.getTaskList());
        babby.printLine("Good job! You completed this task:\n\t\t" + task);
    }

    private void unmark(String input) {
        String[] inputList = input.split(" ");
        if (inputList.length < 2) {
            babby.printLine("Oopsie! You didn't provide a task number! :<");
            return;
        }
        if (!inputList[1].matches("\\d+")) {
            babby.printLine("Oopsie! The task number must be a positive integer! :<");
            return;
        }
        int taskNumber = Integer.parseInt(inputList[1]);
        if (taskNumber < 1 || taskNumber > babby.getTaskList().size()) {
            babby.printLine("Oopsie! The task number " + taskNumber + " does not exist! :<");
            return;
        }
        int index = taskNumber - 1;
        Task task = babby.getTaskList().get(index);
        task.setToDo();
        babby.getStorage().saveTasks(babby.getTaskList());
        babby.printLine("Okay, you need to do this task:\n\t\t" + task);
    }

    private void delete(String input) {
        String[] inputList = input.split(" ");
        if (inputList.length < 2) {
            babby.printLine("Oopsie! You didn't provide a task number! :<");
            return;
        }
        if (!inputList[1].matches("\\d+")) {
            babby.printLine("Oopsie! The task number must be a positive integer! :<");
            return;
        }
        int taskNumber = Integer.parseInt(inputList[1]);
        if (taskNumber < 1 || taskNumber > babby.getTaskList().size()) {
            babby.printLine("Oopsie! The task number " + taskNumber + " does not exist! :<");
            return;
        }
        int index = taskNumber - 1;
        Task task = babby.getTaskList().remove(index);
        babby.getStorage().saveTasks(babby.getTaskList());
        babby.printLine("Okies, I deleted this task:" + task);
        babby.printLine("You have " + babby.getTaskList().size() + " tasks in the list now!");
    }

    private void find(String input) {
        String[] parts = input.split(" ", 2);
        if (parts.length < 2 || parts[1].isBlank()) {
            babby.printLine("Oopsie! You didn't provide a search query! :<");
            return;
        }
        String query = parts[1].trim();
        babby.find(query);
    }

    // Command enums (migrated from Babby)
    private enum Command {
        TODO("todo"),
        DEADLINE("deadline"),
        EVENT("event"),
        LIST("list"),
        FIND("find"),
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
}

