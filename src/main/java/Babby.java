import java.util.*;

public class Babby {
    // List to store user input
    private static ArrayList<Task> taskList = new ArrayList<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in); // Make scanner

        String logo = " ______        _     _           _ \n(____  \\      | |   | |         | |\n ____)  )_____| |__ | |__  _   _| |\n|  __  ((____ |  _ \\|  _ \\| | | |_|\n| |__)  ) ___ | |_) ) |_) ) |_| |_ \n|______/\\_____|____/|____/ \\__  |_|\n                          (____/   ";
        System.out.println("Hello! I'm\n" + logo +"\nSo nice to meet you! Lets be friends <3\n----------------------------------\n");
        // To do: Create a commands object to store the command, the purpose, and the code to run it
        System.out.println("What can I do for you?\nlist -> Lists all tasks\nmark [task number] -> Marks the task as done\nunmark [task number] -> Marks the task as not done\nbye -> Exits the program\n");

        // Start echo
        while (true) {
            String input = scanner.nextLine();
            if (input.equals("bye")) break;
            if (input.equals("list")) list();
            else { // Add task to inputList
                inputList.add(input);
                System.out.println("\tAdded: "+input);
            }
        }
        System.out.println("See you again soon!");
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