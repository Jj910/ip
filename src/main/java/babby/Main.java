package babby;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * A GUI for Babby using FXML.
 */
public class Main extends Application {

    private final Babby BABBY = new Babby("data/tasks.txt");

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);
            stage.setScene(scene);
            stage.setTitle("Babby | Your little companion friend <3");
            fxmlLoader.<babby.ui.MainWindow>getController().setBabby(BABBY);  // inject the Babby instance
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
