/**
 * Bet Recorder - program to record and track bets
 * CMSY167 Spring 2023
 * @author Samuel Dolle
 * @version 1.0
 *
 */
package cmsy182fp.cmsy182finalproject;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    private static Scene scene;
    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("betrecorder"));
        stage.setTitle("Bet Recorder");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show(); //load javafx scene
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load(); //load javafx fxml file
    }
    public static void main(String[] args) {
        launch();
    }
}
