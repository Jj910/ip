package babby.parser;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import babby.Babby;
import babby.task.Deadline;
import babby.task.Event;
import babby.task.Task;
import babby.task.ToDo;

/**
 * Class that parses input data and calls the right commands.
 */
public class Parser {
    private static final DateTimeFormatter INPUT_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HHmm");
    private final Babby babby;
    /**
     * Creates a Parser instance associated with the given Babby application.
     *
     * @param babby the Babby application instance
     */
    public Parser(Babby babby) {
        this.babby = babby;
    }

    /**
     * Parse the input and execute the corresponding command on the application.
     *
     * @param input raw user input line
     * @return true to continue the main loop, false to exit (on byeCommand)
     */
    public boolean parseAndExecute(String input) {
        String commandToken = "";
        if (input != null && !input.isBlank()) {
            String[] parts = input.split(" ");
            if (parts.length > 0) {
                commandToken = parts[0];
            }
        }

        // Normalize input to non-null to avoid passing null into methods
        String notNullInput = (input == null) ? "" : input;

        // Add new commands to the command enums too
        switch (Command.parse(commandToken)) {
        case TODO -> todo(notNullInput);
        case DEADLINE -> deadline(notNullInput);
        case EVENT -> event(notNullInput);
        case LIST -> babby.list();
        case FIND -> find(notNullInput);
        case MARK -> mark(notNullInput);
        case UNMARK -> unmark(notNullInput);
        case DELETE -> delete(notNullInput);
        case HELP -> babby.help();
        case BYE -> {
            babby.byeCommand();
            return false;
        }
        default -> babby.printLine("I'm sorry, I didn't quite get that :<\n\tCould you try again?");
        }
        return true;
    }

    /**
     * Parse the input and execute the corresponding command on the application.
     *
     * @param input raw user input line
     * @return String output representing the result of the command
     */
    public String parseAndReturnOutput(String input) {
        String commandToken = "";
        if (input != null && !input.isBlank()) {
            String[] parts = input.split(" ");
            if (parts.length > 0) {
                commandToken = parts[0];
            }
        }

        // Normalize input to non-null to avoid passing null into methods
        String notNullInput = (input == null) ? "" : input;

        // Add new commands to the command enums too
        switch (Command.parse(commandToken)) {
        case TODO -> {
            return getTodoOutput(notNullInput);
        }
        case DEADLINE -> {
            return getDeadlineOutput(notNullInput);
        }
        case EVENT -> {
            return getEventOutput(notNullInput);
        }
        case LIST -> {
            return getListOutput();
        }
        case FIND -> {
            return getFindOutput(notNullInput);
        }
        case MARK -> {
            return getMarkOutput(notNullInput);
        }
        case UNMARK -> {
            return getUnmarkOutput(notNullInput);
        }
        case DELETE -> {
            return getDeleteOutput(notNullInput);
        }
        case HELP -> {
            return getHelpOutput();
        }
        case BYE -> {
            babby.byeCommand();
            return getByeOutput();
        }
        default -> {
            return "I'm sorry, I didn't quite get that :<\n\tCould you try again?";
        }
        }
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

    /**
     * Parse and return output for todo command
     * @param input Title of the todo task
     * @return String output
     */
    private String getTodoOutput(String input) {
        String[] inputList = input.split("todo ");
        if (inputList.length < 2 || inputList[1].isBlank()) {
            return "Oopsie! The description of a task cannot be empty :<";
        }
        ToDo task = new ToDo(inputList[1]);
        babby.getTaskList().add(task);
        babby.getStorage().saveTasks(babby.getTaskList());
        return "Okay, I added this task: " + task + "\n"
                + "You have " + babby.getTaskList().size() + " tasks in the list now!";
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

    /**
     * Parse and return output for deadline command
     * @param input Title and by time of the deadline task in the format "deadline {title} /by {DD/MM/YYYY HHMM}"
     * @return String output
     */
    private String getDeadlineOutput(String input) {
        String[] inputList = input.replaceFirst("deadline ", "").split(" /by ");
        if (inputList.length < 2 || inputList[0].isBlank() || inputList[1].isBlank()) {
            return "Oopsie! You didn't follow the command's format! :<"
                    + "\nTry something like \"deadline meet friends /by 31/12/2025 2359\"";
        }
        try {
            LocalDateTime by = LocalDateTime.parse(inputList[1], INPUT_FORMATTER);
            Deadline task = new Deadline(inputList[0], by);
            babby.getTaskList().add(task);
            babby.getStorage().saveTasks(babby.getTaskList());
            return "Okay, I added this task: " + task
                    + "\nYou have " + babby.getTaskList().size() + " tasks in the list now!";
        } catch (DateTimeParseException e) {
            return "Oopsie! The date/time you provided is wrong. Try something like 31/12/2025 2359";
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

    /**
     * Parse and return output for event command
     * @param input Title, from time and to time of the event task in the
     *              format "event {title} /from {DD/MM/YYYY HHMM} /to {DD/MM/YYYY HHMM}"
     * @return String output
     */
    private String getEventOutput(String input) {
        String[] inputList = input.replaceFirst("event ", "").split(" /from | /to ");
        if (inputList.length < 3 || inputList[0].isBlank() || inputList[1].isBlank() || inputList[2].isBlank()) {
            return "Oopsie! You didn't follow the command's format! :<"
                    + "\nTry something like \"meet friends /from 01/01/2025 1400 /to 01/01/2025 1600\"";
        }
        try {
            LocalDateTime from = LocalDateTime.parse(inputList[1], INPUT_FORMATTER);
            LocalDateTime to = LocalDateTime.parse(inputList[2], INPUT_FORMATTER);
            Event task = new Event(inputList[0], from, to);
            babby.getTaskList().add(task);
            babby.getStorage().saveTasks(babby.getTaskList());
            return "Okay, I added this task: " + task
                    + "\nYou have " + babby.getTaskList().size() + " tasks in the list now!";
        } catch (DateTimeParseException e) {
            return "Oopsie! The date/time you provided is wrong. Please use DD/MM/YYYY HHMM";
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

    /**
     * Return variants of the commands' outputs (string-returning helpers)
     */
    private String getListOutput() {
        if (babby.getTaskList().isEmpty()) {
            return "\tYour task list is empty! Add some tasks first :)";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("\tHere are your tasks:\n");
        int i = 1;
        for (Task task : babby.getTaskList()) {
            sb.append("\t\t").append(i).append(") ").append(task).append("\n");
            i++;
        }
        // remove trailing newline
        if (!sb.isEmpty() && sb.charAt(sb.length() - 1) == '\n') {
            sb.setLength(sb.length() - 1);
        }
        return sb.toString();
    }

    private String getFindOutput(String input) {
        String[] parts = input.split(" ", 2);
        if (parts.length < 2 || parts[1].isBlank()) {
            return "Oopsie! You didn't provide a search query! :<";
        }
        String query = parts[1].trim().toLowerCase();
        StringBuilder sb = new StringBuilder();
        sb.append("\tHere are the matching tasks in your list:\n");
        int matchIndex = 1;
        for (Task task : babby.getTaskList()) {
            String title = (task.getTitle() == null) ? "" : task.getTitle().toLowerCase();
            String full = (task.toString() == null) ? "" : task.toString().toLowerCase();
            if (title.contains(query) || full.contains(query)) {
                sb.append("\t\t").append(matchIndex).append(") ").append(task).append("\n");
                matchIndex++;
            }
        }
        if (matchIndex == 1) {
            return "\tNo matching tasks found.";
        }
        if (!sb.isEmpty() && sb.charAt(sb.length() - 1) == '\n') {
            sb.setLength(sb.length() - 1);
        }
        return sb.toString();
    }

    private String getMarkOutput(String input) {
        String[] inputList = input.split(" ");
        if (inputList.length < 2) {
            return "Oopsie! You didn't provide a task number! :<";
        }
        if (!inputList[1].matches("\\d+")) {
            return "Oopsie! The task number must be a positive integer! :<";
        }
        int taskNumber = Integer.parseInt(inputList[1]);
        if (taskNumber < 1 || taskNumber > babby.getTaskList().size()) {
            return "Oopsie! The task number " + taskNumber + " does not exist! :<";
        }
        int index = taskNumber - 1;
        Task task = babby.getTaskList().get(index);
        task.setDone();
        babby.getStorage().saveTasks(babby.getTaskList());
        return "Good job! You completed this task:\n\t\t" + task;
    }

    private String getUnmarkOutput(String input) {
        String[] inputList = input.split(" ");
        if (inputList.length < 2) {
            return "Oopsie! You didn't provide a task number! :<";
        }
        if (!inputList[1].matches("\\d+")) {
            return "Oopsie! The task number must be a positive integer! :<";
        }
        int taskNumber = Integer.parseInt(inputList[1]);
        if (taskNumber < 1 || taskNumber > babby.getTaskList().size()) {
            return "Oopsie! The task number " + taskNumber + " does not exist! :<";
        }
        int index = taskNumber - 1;
        Task task = babby.getTaskList().get(index);
        task.setToDo();
        babby.getStorage().saveTasks(babby.getTaskList());
        return "Okay, you need to do this task:\n\t\t" + task;
    }

    private String getDeleteOutput(String input) {
        String[] inputList = input.split(" ");
        if (inputList.length < 2) {
            return "Oopsie! You didn't provide a task number! :<";
        }
        if (!inputList[1].matches("\\d+")) {
            return "Oopsie! The task number must be a positive integer! :<";
        }
        int taskNumber = Integer.parseInt(inputList[1]);
        if (taskNumber < 1 || taskNumber > babby.getTaskList().size()) {
            return "Oopsie! The task number " + taskNumber + " does not exist! :<";
        }
        int index = taskNumber - 1;
        Task task = babby.getTaskList().remove(index);
        babby.getStorage().saveTasks(babby.getTaskList());
        return "Okies, I deleted this task:" + task + "\nYou have "
                + babby.getTaskList().size() + " tasks in the list now!";
    }

    @SuppressWarnings("checkstyle:Regexp")
    private String getHelpOutput() {
        return """
                ToDo {task} -> Adds a todo task
                Deadline {task} /by {DD/MM/YYYY HHMM} -> Adds a deadline task
                Event {task} /from {DD/MM/YYYY HHMM} /to {DD/MM/YYYY HHMM} -> Adds a event task
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

    private String getByeOutput() {
        return "Byebyee! See you again soon!";
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
        BYE("byeCommand"),
        UNKNOWN("");

        private final String command;

        Command(String command) {
            this.command = command;
        }

        public static Command parse(String input) {
            if (input == null || input.isEmpty()) {
                return UNKNOWN;
            }
            for (Command command : values()) {
                if (input.toLowerCase().equals(command.command)) {
                    return command;
                }
            }
            return UNKNOWN;
        }
    }
}
