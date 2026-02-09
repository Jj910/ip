package babby.ui;

import org.junit.jupiter.api.Test;

public class UiTest {
    @Test
    public void testUiMethodsDoNotThrow() {
        Ui ui = new Ui();
        ui.printLine("hello");
        ui.printHelpMessage();
        ui.printWelcomeMessage();
        ui.printGoodbyeMessage();
    }
}
