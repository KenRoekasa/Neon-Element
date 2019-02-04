package client;

import client.GameState;
import client.InputHandler;
import controllers.PowerUpController;
import debugger.Debugger;
import entities.CollisionDetection;
import entities.PhysicsObject;
import entities.Player;
import entities.PowerUp;
import enums.ObjectType;
import graphics.Renderer;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.*;


public class ClientBoard {

    private Debugger debugger;
    private GraphicsContext gc;
    private Stage primaryStage;


    private Rectangle stageSize;
    private ArrayList<String> input;

    private GameState gameState;


    public ClientBoard(Stage primaryStage, GameState gameState) throws Exception {
        // initial setup
        this.primaryStage = primaryStage;
        this.gameState = gameState;

        // load hud
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../userInterface/game_board.fxml"));
        Pane hudPane = new Pane();
        try {
            hudPane = (Pane) loader.load();
            //primaryStage.getScene().getRoot().getChildrenUnmodifiable().setAll((Node) loader.load());

        } catch (Exception e) {
            // todo make this better
            System.out.println("Crash in loading hud in map");
            e.printStackTrace();
            Platform.exit();
            System.exit(0);
        }

        Scene theScene = new Scene(hudPane);

        //Scene theScene = primaryStage.getScene();

        primaryStage.setScene(theScene);
        primaryStage.setFullScreen(true);

        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        stageSize = new Rectangle(primaryStage.getWidth(), primaryStage.getHeight());

        Canvas canvas = new Canvas(primaryScreenBounds.getWidth(), primaryScreenBounds.getHeight());

        hudPane.getChildren().add(canvas);

        gc = canvas.getGraphicsContext2D();
        debugger = new Debugger(gc);

        // initialise input controls
        initialiseInput(theScene);


        Renderer renderer = new Renderer(gc, stageSize, debugger);


        beginClientLoop(renderer);


    }

    private void beginClientLoop(Renderer renderer) {
        new AnimationTimer() {
            public void handle(long currentNanoTime) {
                clientLoop();
                renderer.render(primaryStage, gameState);

            }
        }.start();

        Thread powerUpController = new Thread(new PowerUpController(gameState.getObjects()));
        powerUpController.start();


    }


    private void initialiseInput(Scene theScene) {
        // set input controls
        input = new ArrayList<>();
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
        theScene.setOnMouseMoved(e -> InputHandler.mouseAngleCalc(gameState.getPlayer(), primaryStage, e));
        theScene.setOnMouseDragged(e -> InputHandler.mouseAngleCalc(gameState.getPlayer(), primaryStage, e));
    }

    private void clientLoop() {
        InputHandler.handleInput(gameState.getPlayer(), input, gameState.getMap());
        synchronized (gameState.getObjects()) {
            // Collision detection code
            synchronized (gameState.getObjects()) {
                for (Iterator<PhysicsObject> itr = gameState.getObjects().iterator(); itr.hasNext(); ) {
                    PhysicsObject e = itr.next();
                    if (CollisionDetection.checkCollision(gameState.getPlayer(), e)) {
                        //If the object is a power up
                        if (e.getTag() == ObjectType.POWERUP) {
                            PowerUp powerUp = (PowerUp) e;
                            ((PowerUp) e).activatePowerUp(gameState.getPlayer());
                            // remove power up from objects array list
                            itr.remove();
                        } else {
                            //The player has collided with e do something
                            gameState.getPlayer().getBounds().getBoundsInParent().getMaxX();
                            gameState.getPlayer().isColliding(e);
                        }
                    } else {
                        gameState.getPlayer().isColliding = false;
                    }
                }
            }

            //Call update function for all physics objects
            gameState.getPlayer().update();
            for (PhysicsObject o : gameState.getObjects()) {
                o.update();
            }
        }


    }


}
