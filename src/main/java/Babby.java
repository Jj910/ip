// My little helper, Babby
import java.util.*;

public class Babby {
    // List to store user input
    private static ArrayList<Task> taskList = new ArrayList<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in); // Make scanner

        String logo = " ______        _     _           _ \n(____  \\      | |   | |         | |\n ____)  )_____| |__ | |__  _   _| |\n|  __  ((____ |  _ \\|  _ \\| | | |_|\n| |__)  ) ___ | |_) ) |_) ) |_| |_ \n|______/\\_____|____/|____/ \\__  |_|\n                          (____/   ";
        System.out.println("Hello! I'm\n" + logo +"\nSo nice to meet you! Lets be friends <3\n----------------------------------\n");
        // To do: Create a commands object to store the command, the purpose, and the code to run it
        System.out.println("What can I do for you?");
        System.out.println("\ttodo [task title] -> Adds a todo task\n");
        System.out.println("\tlist -> Lists all tasks\n");
        System.out.println("\tmark [task number] -> Marks the task as done\n");
        System.out.println("\tunmark [task number] -> Marks the task as not done\n");
        System.out.println("\tbye -> Exits the program\n");

        // Main command loop
        while (true) {
            System.out.println("-------------------------------------");
            String input = scanner.nextLine();

            // Possible commands
            if (input.equals("bye")) break;

            else if (input.equals("list")) list();

            else if (input.matches("mark \\d+")) {
                String[] inputList = input.split(" ");
                int index = Integer.parseInt(inputList[1]) - 1;
                Task task = taskList.get(index);
                task.markDone(); // Mark the task as done
                System.out.println("\tGood job! You completed this task:\n\t\t" + task);
            }

            else if (input.matches("unmark \\d+")) {
                String[] inputList = input.split(" ");
                int index = Integer.parseInt(inputList[1]) - 1;
                Task task = taskList.get(index);
                task.markToDo(); // Mark the task as not done
                System.out.println("\tOkay, you need to do this task:\n\t\t" + task);
            }

            else if (input.matches("todo .+")) {
                String[] inputList = input.split("todo ");
                ToDo task = new ToDo(inputList[1]);
                taskList.add(task);
                System.out.println("\tOkay, I added this task: " + task);
                System.out.println("You have " + taskList.size() + " tasks in the list now!");
            }

            else if (input.matches("deadline .+")) {
                String[] inputList = input.replaceFirst("deadline ", "").split(" /by ");
                Deadline task = new Deadline(inputList[0], inputList[1]);
                taskList.add(task);
                System.out.println("\tOkay, I added this task: " + task);
                System.out.println("You have " + taskList.size() + " tasks in the list now!");
            }

            else if (input.matches("event .+")) {
                String[] inputList = input.replaceFirst("event ", "").split(" /from | /to ");
                Event task = new Event(inputList[0], inputList[1], inputList[2]);
                taskList.add(task);
                System.out.println("\tOkay, I added this task: " + task);
                System.out.println("You have " + taskList.size() + " tasks in the list now!");
            }

            else { // Echo input
                System.out.println(input);
            }
        }
        System.out.println("\tSee you again soon!");
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
}