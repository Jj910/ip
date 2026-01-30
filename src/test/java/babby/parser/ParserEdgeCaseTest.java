package babby.parser;

import babby.Babby;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests that exercise error-handling branches in Parser.parseAndExecute.
 */
public class ParserEdgeCaseTest {

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
        public void help() { outputs.add("HELP"); }

        @Override
        public void bye() { outputs.add("BYE"); }

        public List<String> getOutputs() { return outputs; }
    }

    @Test
    public void testTodoEmptyDescription() {
        TestBabby babby = new TestBabby("data/tmp_parser_empty.txt");
        Parser parser = new Parser(babby);

        boolean cont = parser.parseAndExecute("todo ");
        assertTrue(cont);
        assertTrue(babby.getOutputs().stream().anyMatch(s -> s.contains("description of a task cannot be empty") || s.contains("cannot be empty")));

        // also test completely empty input
        babby.getOutputs().clear();
        cont = parser.parseAndExecute("");
        assertTrue(cont);
        // empty input should trigger UNKNOWN branch message
        assertTrue(babby.getOutputs().stream().anyMatch(s -> s.toLowerCase().contains("didn't quite get") || s.toLowerCase().contains("sorry")));
    }

    @Test
    public void testDeadlineMalformedAndBadDate() {
        TestBabby babby = new TestBabby("data/tmp_parser_deadline.txt");
        Parser parser = new Parser(babby);

        // missing /by
        babby.getOutputs().clear();
        parser.parseAndExecute("deadline onlytitle");
        assertTrue(babby.getOutputs().stream().anyMatch(s -> s.contains("didn't follow the command's format") || s.toLowerCase().contains("try something like")));

        // bad date format
        babby.getOutputs().clear();
        parser.parseAndExecute("deadline title /by not-a-date");
        assertTrue(babby.getOutputs().stream().anyMatch(s -> s.toLowerCase().contains("date/time") || s.toLowerCase().contains("wrong")));
    }

    @Test
    public void testEventMalformedAndBadDate() {
        TestBabby babby = new TestBabby("data/tmp_parser_event.txt");
        Parser parser = new Parser(babby);

        // missing /from or /to
        babby.getOutputs().clear();
        parser.parseAndExecute("event title /from only");
        assertTrue(babby.getOutputs().stream().anyMatch(s -> s.contains("didn't follow the command's format") || s.toLowerCase().contains("try something like")));

        // bad date format
        babby.getOutputs().clear();
        parser.parseAndExecute("event title /from not-a-date /to not-a-date");
        assertTrue(babby.getOutputs().stream().anyMatch(s -> s.toLowerCase().contains("date/time") || s.toLowerCase().contains("wrong")));
    }

    @Test
    public void testMarkUnmarkDeleteInvalidInputs() {
        TestBabby babby = new TestBabby("data/tmp_parser_mark.txt");
        Parser parser = new Parser(babby);

        // mark without number
        babby.getOutputs().clear();
        parser.parseAndExecute("mark");
        assertTrue(babby.getOutputs().stream().anyMatch(s -> s.contains("didn't provide a task number") || s.toLowerCase().contains("provide a task number")));

        // mark with non-numeric
        babby.getOutputs().clear();
        parser.parseAndExecute("mark abc");
        assertTrue(babby.getOutputs().stream().anyMatch(s -> s.contains("must be a positive integer") || s.toLowerCase().contains("positive integer")));

        // mark out of range
        babby.getOutputs().clear();
        parser.parseAndExecute("mark 5");
        assertTrue(babby.getOutputs().stream().anyMatch(s -> s.contains("does not exist") || s.toLowerCase().contains("does not exist")));

        // delete with invalid number
        babby.getOutputs().clear();
        parser.parseAndExecute("delete notanumber");
        assertTrue(babby.getOutputs().stream().anyMatch(s -> s.contains("must be a positive integer") || s.toLowerCase().contains("positive integer")));
    }

    @Test
    public void testFindMissingQuery() {
        TestBabby babby = new TestBabby("data/tmp_parser_find.txt");
        Parser parser = new Parser(babby);

        babby.getOutputs().clear();
        parser.parseAndExecute("find");
        assertTrue(babby.getOutputs().stream().anyMatch(s -> s.contains("didn't provide a search query") || s.toLowerCase().contains("search query")));
    }
}
