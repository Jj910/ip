package babby.ui;

import babby.Babby;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
/**
 * Controller for the main GUI.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private Babby babby;

    private final Image userImage = new Image(this.getClass().getResourceAsStream("/images/User.jpg"));
    private final Image babbyImage = new Image(this.getClass().getResourceAsStream("/images/Babby.jpg"));
    /**
     * Initializes the main window and binds the scroll pane to the dialog container height.
     */
    public void startBabby(Babby b) {
        babby = b;
        String welcomeText = babby.getWelcomeMessage();
        dialogContainer.getChildren().add(DialogBox.getBabbyDialog(welcomeText, babbyImage));
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing Babby's reply and then appends them to
     * the dialog container. Clears the user input after processing.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        String response = babby.getResponse(input);
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getBabbyDialog(response, babbyImage)
        );
        userInput.clear();
        Platform.runLater(() -> scrollPane.setVvalue(1.0));
    }
}
