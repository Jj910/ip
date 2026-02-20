package babby.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import babby.Babby;
import babby.task.Task;

public class ParserTest {

    // Lightweight test double for Babby that captures outputs instead of printing them
    private static class TestBabby extends Babby {
        private final List<String> outputs = new ArrayList<>();

        public TestBabby(String filepath) {
            super(filepath);
        }

        @Override
        public void printLine(String message) {
            outputs.add(message);
        }

        @Override
        public void list() {
            if (getTaskList().isEmpty()) {
                outputs.add("LIST: empty");
                return;
            }
            outputs.add("LIST: start");
            int i = 1;
            for (Task t : getTaskList()) {
                outputs.add(i + ") " + t.toString());
                i++;
            }
            outputs.add("LIST: end");
        }

        @Override
        public void find(String input) {
            outputs.add("FIND: " + input);
            int i = 1;
            for (Task t : getTaskList()) {
                if ((t.getTitle() != null && t.getTitle().toLowerCase().contains(input.toLowerCase()))
                        || t.toString().toLowerCase().contains(input.toLowerCase())) {
                    outputs.add("MATCH " + i + ": " + t.toString());
                    i++;
                }
            }
            if (i == 1) {
                outputs.add("FIND: nomatch");
            }
        }

        @Override
        public void help() {
            outputs.add("HELP");
        }

        @Override
        public void byeCommand() {
            outputs.add("BYE");
        }

        public List<String> getOutputs() {
            return outputs;
        }
    }

    @Test
    public void testTodoDeadlineEventAndUnknown() {
        TestBabby babby = new TestBabby("data/test_parser.txt");
        Parser parser = new Parser(babby);

        // TODO
        boolean cont = parser.parseAndExecute("todo buy milk");
        assertTrue(cont);
        assertEquals(1, babby.getTaskList().size());
        assertTrue(babby.getOutputs().stream().anyMatch(s -> s.contains("Okay, I added this task")));

        // DEADLINE
        cont = parser.parseAndExecute("deadline finish hw /by 31/12/2025 2359");
        assertTrue(cont);
        assertEquals(2, babby.getTaskList().size());
        assertTrue(babby.getOutputs().stream().anyMatch(s -> s.contains("Okay, I added this task")));

        // EVENT
        cont = parser.parseAndExecute("event party /from 01/01/2025 1400 /to 01/01/2025 1600");
        assertTrue(cont);
        assertEquals(3, babby.getTaskList().size());
        assertTrue(babby.getOutputs().stream().anyMatch(s -> s.contains("Okay, I added this task")));

        // FRIEND
        cont = parser.parseAndExecute("friend Alice /number 12345678");
        assertTrue(cont);
        assertEquals(4, babby.getTaskList().size());
        assertTrue(babby.getOutputs().stream().anyMatch(s -> s.contains("added this friend")));

        // UNKNOWN
        cont = parser.parseAndExecute("nonsensecommand");
        assertTrue(cont);
        assertTrue(babby.getOutputs().stream()
                .anyMatch(
                s -> s.toLowerCase().contains("i'm sorry") || s.toLowerCase().contains("i didn't quite get")
                ));
    }

    @Test
    public void testMarkUnmarkDeleteListFindHelpBye() {
        TestBabby babby = new TestBabby("data/test_parser.txt");
        Parser parser = new Parser(babby);

        // add two todos
        parser.parseAndExecute("todo first");
        parser.parseAndExecute("todo second");
        assertEquals(2, babby.getTaskList().size());

        // mark 1
        parser.parseAndExecute("mark 1");
        assertTrue(babby.getTaskList().get(0).getIsComplete());
        assertTrue(babby.getOutputs().stream()
                .anyMatch(s -> s.contains("Good job") || s.contains("completed this task")));

        // unmark 1
        parser.parseAndExecute("unmark 1");
        assertFalse(babby.getTaskList().get(0).getIsComplete());

        // list
        babby.getOutputs().clear();
        parser.parseAndExecute("list");
        assertTrue(babby.getOutputs().stream().anyMatch(s -> s.startsWith("LIST:")));

        // find (match)
        babby.getOutputs().clear();
        parser.parseAndExecute("find first");
        assertTrue(babby.getOutputs().stream().anyMatch(s -> s.startsWith("MATCH") || s.startsWith("FIND:")));

        // delete 1
        parser.parseAndExecute("delete 1");
        assertEquals(1, babby.getTaskList().size());

        // help
        babby.getOutputs().clear();
        parser.parseAndExecute("help");
        assertTrue(babby.getOutputs().contains("HELP"));

        // byeCommand
        babby.getOutputs().clear();
        boolean cont = parser.parseAndExecute("byeCommand");
        assertFalse(cont);
        assertTrue(babby.getOutputs().contains("BYE"));
    }
}
