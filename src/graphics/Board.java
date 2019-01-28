package graphics;

import debugger.Debugger;
import entities.CollisionDetection;
import entities.Player;
import entities.PowerUp;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
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


        // stop collision detector when leaving the game - otherwise it never gets stopped
        // todo show this to kenny
        primaryStage.setOnCloseRequest(t -> {
            Platform.exit();
            System.exit(0);
        });

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
                //renderer.drawerCursor(stageSize, player);
                //renderer.drawCrosshair(stageSize);
                renderer.drawPlayer(stageSize, player);
                //renderer.drawEnemies(stageSize, enemies, player);

                debugger.add((player.getLocation().toString()),1);

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
        board = new Rectangle(1500, 1500);

        //Collsion Detection loop
        player = new Player();

        //TODO: Remove
        //add a powerup
        (new PowerUp(player)).start();


        // set player location to the top left of the map

        Point2D playerStartLocation = new Point2D(500, 500);

        player.setLocation(playerStartLocation);

        enemies = new ArrayList<>();
        enemies.add(new Player());
        enemies.get(0).setLocation(new Point2D(140,100));

        //detect collisions
        CollisionDetection colDetection = new CollisionDetection(player, enemies);
        colDetection.start();

    }

    private void handleInput(ArrayList<String> input) {
        boolean left = input.contains("LEFT") || input.contains("A");
        boolean right = input.contains("RIGHT") || input.contains("D");
        boolean up = input.contains("UP") || input.contains("W");
        boolean down = input.contains("DOWN") || input.contains("S");


        if (left && up || left & down || right && up || right & down) {
            if (left & up) {
                player.moveLeftCartesian();
            }
            if (left && down) {
                player.moveDownCartestian(board.getHeight());
            }

            if (right && up) {
                player.moveUpCartesian();
            }

            if (right && down){
                player.moveRightCartesian(board.getWidth());
            }
        } else {
            moveIsometric(left, right, up, down);
        }

    }

    private void moveIsometric(boolean left, boolean right, boolean up, boolean down) {
        if (left) {
            player.moveLeft(board.getWidth());
        }

        if (up) {
            player.moveUp();
        }

        if (right) {
            player.moveRight(board.getWidth(), board.getHeight());
        }
        if (down) {
            player.moveDown(board.getWidth(), board.getHeight());
        }
    }

}
