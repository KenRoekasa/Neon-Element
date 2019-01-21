package Graphics;

import Debugger.Debugger;
import Entities.Player;
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

    private Rectangle board;
    private Rectangle stageSize;

    private Player player;

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

        stageSize = new Rectangle(primaryStage.getWidth(), primaryStage.getHeight());

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
                // clear screen
                gc.clearRect(0, 0, primaryStage.getWidth(), primaryStage.getHeight());

                // update screen size
                // todo check whether this is needed
                stageSize = new Rectangle(primaryStage.getWidth(), primaryStage.getHeight());

                handleInput(input);

                // draw to screen
                renderer.drawMap(stageSize, board, player);
                renderer.drawPlayer(stageSize, player);

                // debug info
                String debug = "Player location: " + player.getLocation().toString();
                debugger.add(debug, 1);
                debugger.print();
            }
        }.start();

        primaryStage.show();

    }

    private void initialise() {
        debugger = new Debugger(gc);
        renderer = new Renderer(gc, debugger);

        //initialise map location
        board = new Rectangle(1000, 1000);

        player = new Player();

        // set player location to the top left of the map

        Point2D playerStartLocation = new Point2D(0, 0);

        player.setLocation(playerStartLocation);
    }


    private void handleInput(ArrayList<String> input) {
        if (input.contains("LEFT")) {
            player.moveLeft();
        }
        if (input.contains("RIGHT")) {
            player.moveRight(board.getWidth());
        }
        if (input.contains("UP")) {
            player.moveUp();
        }
        if (input.contains("DOWN")) {
            player.moveDown(board.getHeight());
        }
    }

}
