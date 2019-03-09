
import client.audiomanager.AudioManager;
import graphics.userInterface.controllers.MenuController;
import graphics.userInterface.controllers.UIController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Menu extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        FXMLLoader loader = new FXMLLoader(getClass().getResource("graphics/userInterface/fxmls/menu.fxml" ));

        Parent root = loader.load();
        primaryStage.setTitle("Menu");
        MenuController controller = loader.getController();
        controller.setStage(primaryStage);

        Rectangle2D primaryScreenBounds = Screen.getPrimary().getBounds();
        Double width = primaryScreenBounds.getWidth();
        Double height = primaryScreenBounds.getHeight();

        primaryStage.setFullScreen(true);
        primaryStage.setResizable(false);
        // prevent fullscreen popup
        primaryStage.setFullScreenExitHint("");


        //force screen size
        primaryStage.setMinWidth(width);
        primaryStage.setMinHeight(height);

        Scene scene = new Scene(root, width, height);
        primaryStage.setScene(scene);

        AudioManager audioManager = new AudioManager();
        controller.setAudioManager(audioManager);


        // stops all game threads on close
        primaryStage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

