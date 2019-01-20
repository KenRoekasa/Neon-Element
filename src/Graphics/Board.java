package Graphics;

import Debugger.Debugger;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.ArrayList;


public class Board extends Application {

    private Debugger debugger;
    private Renderer renderer;
    private GraphicsContext gc;
    private Stage primaryStage;

    private Point2D mapPosition;
    private Point2D playerLocation;
    private Rectangle board;

    private int playerWidth;
    private double playerX;
    private double playerY;
    private int playerSpeed;

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage stage) {
        // initial setup
        primaryStage = stage;


        primaryStage.setTitle("Game");

        Group root = new Group();
        Scene theScene = new Scene(root);
        primaryStage.setScene(theScene);

        primaryStage.setFullScreen(true);
        primaryStage.setResizable(false);

        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();

        // set Stage boundaries to visible bounds of the main screen
        primaryStage.setX(primaryScreenBounds.getMinX());
        primaryStage.setY(primaryScreenBounds.getMinY());
        primaryStage.setWidth(primaryScreenBounds.getWidth());
        primaryStage.setHeight(primaryScreenBounds.getHeight());

        Canvas canvas = new Canvas(primaryScreenBounds.getWidth(), primaryScreenBounds.getHeight());

        root.getChildren().add(canvas);

        // set input controls
        ArrayList<String> input = new ArrayList<String>();
        theScene.setOnKeyPressed(e -> {
            String code = e.getCode().toString();

            // only add once... prevent duplicates
            if (!input.contains(code))
                input.add(code);
        });

        theScene.setOnKeyReleased(
                e -> {
                    String code = e.getCode().toString();
                    input.remove(code);
                });

        gc = canvas.getGraphicsContext2D();

        initialise();

        // tic 60 per sec
        new AnimationTimer() {
            public void handle(long currentNanoTime) {
                gc.clearRect(0,0,primaryStage.getWidth(),primaryStage.getHeight());

                handleInput(input);

                // update player location
                playerLocation = new Point2D(playerX - mapPosition.getX(), playerY - mapPosition.getY());

                renderer.drawMap(mapPosition, playerLocation, board);
                renderer.drawPlayer(playerX, playerY, playerWidth);

                debugger.print();
            }
        }.start();

        primaryStage.show();

    }

    private void initialise(){
        debugger = new Debugger(gc);
        renderer = new Renderer(gc, debugger);

        //initialise map location
        board = new Rectangle(2000,2000);

        mapPosition = new Point2D(0,0);

        // set player location to the center of the screen
        playerX =  primaryStage.getWidth() / 2;
        playerY =  primaryStage.getHeight() / 2;
        playerWidth = 20;
        playerSpeed = 10;

        playerLocation = new Point2D(playerX - mapPosition.getX(), playerY - mapPosition.getY());
    }


    private void handleInput(ArrayList<String> input){
        if (input.contains("LEFT")){
            //check within bounds
            if((playerLocation.getX() - playerSpeed - playerWidth/2) >= 0){
                mapPosition = mapPosition.add(playerSpeed,0);
            }
        }
        if (input.contains("RIGHT")){
            //check within bounds
            if((playerLocation.getX() + playerSpeed) < board.getWidth()){
                mapPosition = mapPosition.add(- playerSpeed,0);

            }
        }
        if (input.contains("UP")){
            if((playerLocation.getY() - playerSpeed) > 0){
                mapPosition = mapPosition.add(0,playerSpeed);
            }
        }
        if (input.contains("DOWN")){
            if((playerLocation.getY() + playerSpeed) < board.getHeight()){
                mapPosition = mapPosition.add(0,-playerSpeed);
            }
        }
    }

}
