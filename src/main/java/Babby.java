// My little helper, Babby
import java.util.*;

public class Babby {
    // List to store user input
    private static ArrayList<Task> taskList = new ArrayList<>();

    // Command enums

    private static enum Command {
        TODO("todo"),
        DEADLINE("deadline"),
        EVENT("event"),
        LIST("list"),
        MARK("mark"),
        UNMARK("unmark"),
        DELETE("delete"),
        BYE("bye"),
        UNKNOWN("");

        private String command;

        private Command(String command) {
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

        String logo = " ______        _     _           _ \n(____  \\      | |   | |         | |\n ____)  )_____| |__ | |__  _   _| |\n|  __  ((____ |  _ \\|  _ \\| | | |_|\n| |__)  ) ___ | |_) ) |_) ) |_| |_ \n|______/\\_____|____/|____/ \\__  |_|\n                          (____/   ";
        System.out.println("Hello! I'm\n" + logo +"\nSo nice to meet you! Lets be friends <3\n----------------------------------\n");
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
                case TODO:
                    todo(input);
                    break;
                case DEADLINE:
                    deadline(input);
                    break;
                case EVENT:
                    event(input);
                    break;
                case LIST:
                    list();
                    break;
                case MARK:
                    mark(input);
                    break;
                case UNMARK:
                    unmark(input);
                    break;
                case DELETE:
                    delete(input);
                    break;
                case BYE:
                    System.out.println("\tByebyee! See you again soon!");
                    return;
                case UNKNOWN:
                default:
                    System.out.println("\tI'm sorry, I didn't quite get that :<\n\tCould you try again?");
                    break;
            }
        }
    }

    public static void todo(String input) {
        String[] inputList = input.split("todo ");
        ToDo task = new ToDo(inputList[1]);
        taskList.add(task);
        System.out.println("\tOkay, I added this task: " + task);
        System.out.println("\tYou have " + taskList.size() + " tasks in the list now!");
    }

    public static void deadline(String input) {
        String[] inputList = input.replaceFirst("deadline ", "").split(" /by ");
        Deadline task = new Deadline(inputList[0], inputList[1]);
        taskList.add(task);
        System.out.println("\tOkay, I added this task: " + task);
        System.out.println("\tYou have " + taskList.size() + " tasks in the list now!");
    }

    public static void event(String input) {
        String[] inputList = input.replaceFirst("event ", "").split(" /from | /to ");
        Event task = new Event(inputList[0], inputList[1], inputList[2]);
        taskList.add(task);
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
        return;
    }

    public static void mark(String input) {
        String[] inputList = input.split(" ");
        int index = Integer.parseInt(inputList[1]) - 1;
        Task task = taskList.get(index);
        task.markDone(); // Mark the task as done
        System.out.println("\tGood job! You completed this task:\n\t\t" + task);
    }

    public static void unmark(String input) {
        String[] inputList = input.split(" ");
        int index = Integer.parseInt(inputList[1]) - 1;
        Task task = taskList.get(index);
        task.markToDo(); // Mark the task as not done
        System.out.println("\tOkay, you need to do this task:\n\t\t" + task);
    }

    public static void delete(String input) {
        String[] inputList = input.split(" ");
        int index = Integer.parseInt(inputList[1]) - 1;
        Task task = taskList.remove(index);
        System.out.println("\tOkies, I deleted this task:" + task);
        System.out.println("\tYou have " + taskList.size() + " tasks in the list now!");
    }
}