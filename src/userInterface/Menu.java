package userInterface;

import controllers.MenuController;
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

        FXMLLoader loader = new FXMLLoader(getClass().getResource("menu.fxml"));
        Parent root = (Parent)loader.load();
        primaryStage.setTitle("Game");
       
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getBounds();
        Double width = primaryScreenBounds.getWidth();
        Double height = primaryScreenBounds.getHeight();

        primaryStage.setFullScreen(true);
        primaryStage.setResizable(false);

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

        /*pass current stage to following interactions*/
        MenuController menuController = loader.getController();
        menuController.setStage(primaryStage);
        menuController.setStageSize(primaryScreenBounds);
        primaryStage.show();

    }

    public static void main(String[] args) {
    	System.setProperty("java.net.preferIPv4Stack", "true");

        launch(args);
    }
}

