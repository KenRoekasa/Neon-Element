package userInterface;

import controllers.MenuController;
import javafx.application.Application;
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
        Parent root = loader.load();
        primaryStage.setTitle("Game");
       // System.out.println("helo");

        Rectangle2D primaryScreenBounds = Screen.getPrimary().getBounds();
        Double width = primaryScreenBounds.getWidth();
        Double height = primaryScreenBounds.getHeight();

        //force screen size
        primaryStage.setMinWidth(width);
        primaryStage.setMinHeight(height);
        primaryStage.setMaxWidth(width);
        primaryStage.setMaxHeight(height);

        primaryStage.setScene(new Scene(root, width, height));

        primaryStage.setFullScreen(true);
        primaryStage.setResizable(false);

        /*pass current stage to following interactions*/
        MenuController menuController = (MenuController)loader.getController();
        menuController.setStage(primaryStage);

        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}

