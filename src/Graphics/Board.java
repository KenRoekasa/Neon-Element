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
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Rotate;
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
    private ArrayList<Player> enemies;

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
        ArrayList<String> input = new ArrayList<>();
        theScene.setOnKeyPressed(e -> {
            String code = e.getCode().toString();

            // only add each input command once
            if (!input.contains(code))
                input.add(code);
        });

        theScene.setOnKeyReleased(
                e -> {
                    String code = e.getCode().toString();
                    input.remove(code);
                });


        // when the mouse is moved around the screen calculate new angle
        theScene.setOnMouseMoved(this::mouseAngleCalc);
        theScene.setOnMouseDragged(this::mouseAngleCalc);

        gc = canvas.getGraphicsContext2D();

        initialise();

        // tic 60 per sec
        new AnimationTimer() {
            public void handle(long currentNanoTime) {
                // clear screen
                gc.clearRect(0, 0, primaryStage.getWidth(), primaryStage.getHeight());

                handleInput(input);

                // draw to screen
                renderer.drawMap(stageSize, board, player);
                renderer.drawerCursor(stageSize, player);
                //renderer.drawCrosshair(stageSize);
                renderer.drawPlayer(stageSize, player);
                renderer.drawEnemies(stageSize, enemies, player);

                debugger.print();
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }.start();

        primaryStage.show();

    }

    // this needs to be made much more efficient !!!!
    // possibilities are:
    // threading, faster angle calculation, both
    private void mouseAngleCalc(MouseEvent event) {
        double opposite = primaryStage.getWidth()/2 - event.getX();

        double adjacent = primaryStage.getHeight()/2 - event.getY();

        double angle = Math.atan(Math.abs(opposite)/Math.abs(adjacent));
        angle = Math.toDegrees(angle);

        if(adjacent < 0 && opposite < 0) {
            angle = 180 - angle;
        } else if (adjacent < 0) {
            angle = angle + 180;
        } else if (opposite > 0) {
            angle = 360 - angle;
        }

        if(angle - 45 >= 0 ) {
            angle -= 45;
        } else {
            angle += 315;
        }

        player.setPlayerAngle(new Rotate(angle));
    }

    private void initialise() {
        debugger = new Debugger(gc);
        renderer = new Renderer(gc, debugger);

        //initialise map location
        board = new Rectangle(1000, 1000);

        player = new Player();

        // set player location to the top left of the map

        Point2D playerStartLocation = new Point2D(500, 500);

        player.setLocation(playerStartLocation);

        enemies = new ArrayList<>();
        enemies.add(new Player());
        enemies.get(0).setLocation(new Point2D(100,100));

    }


    private void handleInput(ArrayList<String> input) {
        if (input.contains("LEFT") || input.contains("A")) {
            player.moveLeft();
        }
        if (input.contains("RIGHT") || input.contains("D")) {
            player.moveRight(board.getWidth());
        }
        if (input.contains("UP") || input.contains("W")) {
            player.moveUp();
        }
        if (input.contains("DOWN") || input.contains("S")) {
            player.moveDown(board.getHeight());
        }
    }

}
