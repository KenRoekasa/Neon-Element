import graphics.userInterface.controllers.MenuNewController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class MenuNew extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        FXMLLoader loader = new FXMLLoader(getClass().getResource("graphics/userInterface/fxmls/menu_new.fxml"));


        Parent root = loader.load();
        primaryStage.setTitle("Menu");
        MenuNewController controller = loader.getController();
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

        // stops all game threads on close
        primaryStage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });


        //MenuController menuController = loader.getController();
        //menuController.setStage(primaryStage);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

