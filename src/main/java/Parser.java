/**
 * Class that parses input data and calls the right commands.
 */
public class Parser {
    private final Babby babby;

    public Parser(Babby babby) {
        this.babby = babby;
    }

    /**
     * Parse the input and execute the corresponding command on the application.
     * @param input raw user input line
     * @return true to continue the main loop, false to exit (on bye)
     */
    public boolean parseAndExecute(String input) {
        String cmdToken = "";
        if (input != null && !input.isBlank()) {
            String[] parts = input.split(" ");
            if (parts.length > 0) cmdToken = parts[0];
        }

        // normalize input to non-null to avoid passing null into Babby methods
        String safeInput = (input == null) ? "" : input;

        switch (Command.parse(cmdToken)) {
            case TODO -> babby.todo(safeInput);
            case DEADLINE -> babby.deadline(safeInput);
            case EVENT -> babby.event(safeInput);
            case LIST -> babby.list();
            case MARK -> babby.mark(safeInput);
            case UNMARK -> babby.unmark(safeInput);
            case DELETE -> babby.delete(safeInput);
            case HELP -> babby.help();
            case BYE -> {
                babby.bye();
                return false;
            }
            default -> babby.printLine("I'm sorry, I didn't quite get that :<\n\tCould you try again?");
        }
        return true;
    }

    // Command enums (migrated from Babby)
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
}