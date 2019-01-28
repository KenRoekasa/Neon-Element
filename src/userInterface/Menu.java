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
        Parent root = (Parent)loader.load();
        primaryStage.setTitle("Game");

        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        Double width = primaryScreenBounds.getWidth();
        Double height = primaryScreenBounds.getHeight();

        primaryStage.setWidth(width);
        primaryStage.setHeight(height);
        primaryStage.setScene(new Scene(root, width, height));

        MenuController menuControllercontroller = (MenuController)loader.getController();
        menuControllercontroller.setStage(primaryStage);

        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}

