package babby;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * A GUI for Babby using FXML.
 */
public class Main extends Application {

    private final Babby babby = new Babby("data/tasks.txt");
    private final Image titleIcon = new Image(this.getClass().getResourceAsStream("/images/User.jpg"));

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);
            stage.setMinHeight(220);
            stage.setMinWidth(400);
            stage.setScene(scene);
            stage.setTitle("Babby | Your little companion friend <3");
            stage.getIcons().add(titleIcon);
            fxmlLoader.<babby.ui.MainWindow>getController().startBabby(babby); // inject and start the Babby instance
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
