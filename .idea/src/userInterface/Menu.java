package UserInterface;

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
        Parent root = FXMLLoader.load(getClass().getResource("Menu.fxml"));
        primaryStage.setTitle("Game");

        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        Double width = primaryScreenBounds.getWidth();
        Double height = primaryScreenBounds.getHeight();

        primaryStage.setWidth(width);
        primaryStage.setHeight(height);
        primaryStage.setScene(new Scene(root, width, height));
        primaryStage.show();

    }



    public static void main(String[] args) {
        launch(args);
    }
}

