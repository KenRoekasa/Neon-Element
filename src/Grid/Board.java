package Grid;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;
import javafx.stage.Screen;
import javafx.stage.Stage;

import javax.swing.*;
import java.util.ArrayList;


public class Board extends Application {

    private int mapX;
    private int mapY;


    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) {
        // initial setup
        primaryStage.setTitle("Game");

        Group root = new Group();
        Scene theScene = new Scene(root);
        primaryStage.setScene(theScene);

        primaryStage.setFullScreen(true);
        primaryStage.setResizable(false);

        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();

        //set Stage boundaries to visible bounds of the main screen
        primaryStage.setX(primaryScreenBounds.getMinX());
        primaryStage.setY(primaryScreenBounds.getMinY());
        primaryStage.setWidth(primaryScreenBounds.getWidth());
        primaryStage.setHeight(primaryScreenBounds.getHeight());

        Canvas canvas = new Canvas(primaryScreenBounds.getWidth(), primaryScreenBounds.getHeight());

        root.getChildren().add(canvas);

        // set input controls
        ArrayList<String> input = new ArrayList<String>();
        theScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent e) {
                String code = e.getCode().toString();

                // only add once... prevent duplicates
                if (!input.contains(code))
                    input.add(code);
            }
        });

        theScene.setOnKeyReleased(
                new EventHandler<KeyEvent>() {
                    public void handle(KeyEvent e) {
                        String code = e.getCode().toString();
                        input.remove(code);
                    }
                });


        GraphicsContext gc = canvas.getGraphicsContext2D();

        //initialise map location
        mapX = 972;
        mapY = -1296;

        drawMap(gc);

        // tic 60 per sec
        new AnimationTimer() {
            public void handle(long currentNanoTime) {
                gc.clearRect(0,0,1920,1080);

                if (input.contains("LEFT")){
                    mapX += 4;
                }
                if (input.contains("RIGHT")){
                    mapX -= 4;
                }
                if (input.contains("UP")){
                    mapY += 4;
                }
                if (input.contains("DOWN")){
                    mapY -= 4;
                }

                drawMap(gc);
                drawPlayer(gc);
            }
        }.start();


        primaryStage.show();


    }

    private boolean playerBoundManager(int mapY, int mapX, int playerY, int playerX){

        return false;
    }

    private void drawPlayer(GraphicsContext gc) {
        float width = 20;
        gc.setFill(Color.GREEN);
        gc.fillRect(1920.0/2 - width/2, 1080.0/2 - width / 2, width,width );

    }

    private void drawMap(GraphicsContext gc) {

        // create transformation to rotate by 45 degrees and squash
        gc.save();
        Affine affine = new Affine();
        affine.appendRotation(45, mapX,mapY);
        affine.prependScale(1,0.5);

        gc.transform(affine);
        // draw map
        gc.strokeRect(mapX, mapY, 2000, 2000);

        // restore previous state
        gc.restore();

        //print grid location
        gc.strokeText("X coord: " + mapX + "\nY coord: " + mapY, 20, 20);

    }


}
