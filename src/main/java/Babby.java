import java.util.Scanner;

public class Babby {
    // List to store user input
    private String[] inputList = new String[100];

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in); // Make scanner

        String logo = "┳┓  ┓ ┓   ╻\n" +
                "┣┫┏┓┣┓┣┓┓┏┃\n" +
                "┻┛┗┻┗┛┗┛┗┫•\n" +
                "         ┛ ";
        System.out.println("Hello! I'm\n" + logo +"\nSo nice to meet you! Lets be friends <3\n----------------------------------\nWhat can I do for you?\n[bye] -> Exits the program");



        // Start echo
        while (true) {
            String input = scanner.nextLine();
            if (input.equals("bye")) break;
            else System.out.println("\t"+input);
        }
        System.out.println("See you again soon!");
    }
}