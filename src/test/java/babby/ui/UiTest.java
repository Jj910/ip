package babby.ui;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UiTest {
    @Test
    public void testUiMethodsDoNotThrow() {
        Ui ui = new Ui();
        ui.printLine("hello");
        ui.printHelp();
        ui.printWelcomeMessage();
        ui.printGoodbye();
    }
}
