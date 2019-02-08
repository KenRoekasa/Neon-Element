package client;

import controllers.AttributeController;
import controllers.PowerUpController;
import debugger.Debugger;
import entities.CollisionDetection;
import entities.PhysicsObject;
import entities.PowerUp;
import enums.Action;
import enums.ObjectType;
import graphics.Renderer;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Iterator;


public class ClientBoard {

    private Debugger debugger;
    private GraphicsContext gc;
    private Stage primaryStage;
    private Scene scene;
    private Rectangle stageSize;
    private ArrayList<String> input;
    private GameState gameState;

    public Scene getScene() {
        return scene;
    }

    public ClientBoard(Stage primaryStage, GameState gameState) throws Exception {
        // initial setup
        this.primaryStage = primaryStage;
        this.gameState = gameState;

        // load hud
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../userInterface/game_board.fxml"));
        Pane hudPane = new Pane();

        try {
            hudPane = (Pane) loader.load();
            AttributeController attributeController = loader.getController();
            attributeController.initPlayer(gameState.getPlayer());
        } catch (Exception e) {
            // todo make this better
            System.out.println("Crash in loading hud in map");
            e.printStackTrace();
            Platform.exit();
            System.exit(0);
        }

        scene = new Scene(hudPane);

        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);

        stageSize = new Rectangle(primaryStage.getWidth(), primaryStage.getHeight());

        Canvas canvas = new Canvas(stageSize.getWidth(), stageSize.getHeight());
        hudPane.getChildren().add(canvas);

        //forces the game to be rendered behind the gui
        int index = hudPane.getChildren().indexOf(canvas);
        hudPane.getChildren().get(index).toBack();

        gc = canvas.getGraphicsContext2D();
        debugger = new Debugger(gc);

        Renderer renderer = new Renderer(gc, stageSize, debugger);

        // initialise input controls
        initialiseInput(scene, renderer);

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


    private void initialiseInput(Scene theScene, Renderer renderer) {
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

        theScene.setOnMouseClicked(e -> {
            InputHandler.handleClick(gameState.getPlayer(), primaryStage, e, renderer);


        });


        // when the mouse is moved around the screen calculate new angle
        theScene.setOnMouseMoved(e -> InputHandler.mouseAngleCalc(gameState.getPlayer(), primaryStage, e));
        theScene.setOnMouseDragged(e -> InputHandler.mouseAngleCalc(gameState.getPlayer(), primaryStage, e));
    }

    private void clientLoop() {
        InputHandler.handleKeyboardInput(gameState.getPlayer(), input, gameState.getMap());
        doCollisionDetection();
        doUpdates();
    }

    private void doUpdates() {
        synchronized (gameState.getObjects()) {
            //Call update function for all physics objects
            gameState.getPlayer().update();
            for (PhysicsObject o : gameState.getObjects()) {
                o.update();
            }
        }
    }

    private void doCollisionDetection() {
        synchronized (gameState.getObjects()) {
            // Collision detection code
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
                //Attack Collision
                //if player is attacking check
                if (gameState.getPlayer().getCurrentAction() == Action.LIGHT) {
                    if (CollisionDetection.checkCollision(gameState.getPlayer().getAttackHitbox().getBoundsInParent(), e.getBounds().getBoundsInParent())) {
                        // e takes damage
                        System.out.println("hit");
                    }

                }
            }
        }
    }
}


