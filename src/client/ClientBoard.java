package client;

import client.ClientGameState;
import client.InputHandler;
import controllers.AttributeController;
import debugger.Debugger;
import entities.*;
import enums.Action;
import enums.ObjectType;
import graphics.Renderer;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import server.controllers.PowerUpController;

import java.util.ArrayList;
import java.util.Iterator;

import static enums.Directions.LEFTCART;


public class ClientBoard {

    private Debugger debugger;
    private GraphicsContext gc;
    private Stage primaryStage;
    private Scene scene;
    private Rectangle stageSize;
    private ArrayList<String> input;

    private ClientGameState gameState;
    private GameClient gameClient;

    public Scene getScene() {
        return scene;
    }

    public ClientBoard(Stage primaryStage, ClientGameState gameState) throws Exception {
        // initial setup
        this.primaryStage = primaryStage;
        this.gameState = gameState;

        // load hud
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../userInterface/game_board.fxml"));
        Pane hudPane = new Pane();


        try {
            hudPane = (Pane) loader.load();
            //get player attribute
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
        //hudPane.getChildren().get(index).toBack();

        gc = canvas.getGraphicsContext2D();
        debugger = new Debugger(gc);

        Renderer renderer = new Renderer(gc, stageSize, debugger);

        // initialise input controls
        initialiseInput(scene, renderer);

        beginClientLoop(renderer);

        //this.gameClient = new GameClient(gameState);
        //gameClient.run();
    }

    private void beginClientLoop(Renderer renderer) {
        new AnimationTimer() {
            public void handle(long currentNanoTime) {
                InputHandler.handleKeyboardInput(gameState.getPlayer(), input, gameState.getMap());
                renderer.render(primaryStage, gameState);


            }
        }.start();

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

        ArrayList<PhysicsObject> objects = gameState.getObjects();
        synchronized (objects) {
            // Collision detection code
            gameState.getPlayer().canUp = true;
            gameState.getPlayer().canLeft = true;
            gameState.getPlayer().canRight = true;
            gameState.getPlayer().canDown = true;
            gameState.getPlayer().canUpCart = true;
            gameState.getPlayer().canRightCart = true;
            gameState.getPlayer().canLeftCart = true;
            gameState.getPlayer().canDownCart = true;
            Player projectedPlayer = new Player(ObjectType.PLAYER);
            for (Iterator<PhysicsObject> itr = objects.iterator(); itr.hasNext(); ) {
                PhysicsObject e = itr.next();
                // Check if the moving in a certain direction will cause a collision
                // The player has collided with e do something
                if (e.getTag() == ObjectType.POWERUP) {
                    if (CollisionDetection.checkCollision(gameState.getPlayer(), e)) {
                        PowerUp powerUp = (PowerUp) e;
                        powerUp.activatePowerUp(gameState.getPlayer());
                        // remove power up from objects array list
                        itr.remove();

                    }
                } else {
                    double x = gameState.getPlayer().getLocation().getX();
                    double y = gameState.getPlayer().getLocation().getY();
                    int movementSpeed = gameState.getPlayer().getMovementSpeed();
                    Point2D checkUp = new Point2D(x - movementSpeed, y - movementSpeed);
                    Point2D checkDown = new Point2D(x + movementSpeed, y + movementSpeed);
                    Point2D checkLeft = new Point2D(x - movementSpeed, y + movementSpeed);
                    Point2D checkRight = new Point2D(x + movementSpeed, y - movementSpeed);
                    Point2D checkUpCart = new Point2D(x, y - movementSpeed);
                    Point2D checkDownCart = new Point2D(x, y + movementSpeed);
                    Point2D checkLeftCart = new Point2D(x - movementSpeed, y);
                    Point2D checkRightCart = new Point2D(x + movementSpeed, y);
                    Point2D[] projectedLocations = {checkUp, checkDown, checkLeft, checkRight, checkUpCart, checkDownCart, checkLeftCart, checkRightCart};

                    switch (gameState.getPlayer().getCharacterDirection()) {
                        case UP:
                            projectedPlayer.setLocation(checkUp);
                            if (CollisionDetection.checkCollision(projectedPlayer, e)) {
                                gameState.getPlayer().canUp = false;
                                gameState.getPlayer().canUpCart = false;
                                gameState.getPlayer().canLeftCart = false;
                            }

                            break;
                        case DOWN:
                            projectedPlayer.setLocation(checkDown);
                            if ((CollisionDetection.checkCollision(projectedPlayer, e))) {
                                gameState.getPlayer().canDown = false;
                                gameState.getPlayer().canDownCart = false;
                                gameState.getPlayer().canRightCart = false;
                            }
                            break;
                        case LEFT:
                            projectedPlayer.setLocation(checkLeft);
                            if ((CollisionDetection.checkCollision(projectedPlayer, e))) {
                                gameState.getPlayer().canLeft = false;
                                gameState.getPlayer().canDownCart = false;
                                gameState.getPlayer().canLeftCart = false;
                                gameState.getPlayer().canRightCart = false;

                            }

                            break;
                        case UPCART:
                            projectedPlayer.setLocation(checkUpCart);
                            if ((CollisionDetection.checkCollision(projectedPlayer, e))) {
                                gameState.getPlayer().canUpCart = false;
                                gameState.getPlayer().canUp = false;
                                gameState.getPlayer().canRight = false;
                                gameState.getPlayer().canLeftCart = false;
                            }
                            break;
                        case DOWNCART:
                            projectedPlayer.setLocation(checkDownCart);
                            if ((CollisionDetection.checkCollision(projectedPlayer, e))) {
                                gameState.getPlayer().canDownCart = false;
                                gameState.getPlayer().canDown = false;
                                gameState.getPlayer().canLeft = false;
                                gameState.getPlayer().canRightCart = false;
                            }
                            break;
                        case LEFTCART:
                            projectedPlayer.setLocation(checkLeftCart);
                            if ((CollisionDetection.checkCollision(projectedPlayer, e))) {
                                gameState.getPlayer().canLeftCart = false;
                                gameState.getPlayer().canUp = false;
                                gameState.getPlayer().canLeft = false;
                                gameState.getPlayer().canUpCart = false;
                            }
                            break;
                        case RIGHTCART:
                            projectedPlayer.setLocation(checkRightCart);
                            if ((CollisionDetection.checkCollision(projectedPlayer, e))) {
                                gameState.getPlayer().canRightCart = false;
                                gameState.getPlayer().canDown = false;
                                gameState.getPlayer().canRight = false;
                                gameState.getPlayer().canDownCart = false;
                                gameState.getPlayer().canLeft = false;
                            }
                            break;
                        case RIGHT:
                            projectedPlayer.setLocation(checkRight);
                            if ((CollisionDetection.checkCollision(projectedPlayer, e))) {
                                gameState.getPlayer().canRight = false;
                                gameState.getPlayer().canDown = false;
                                gameState.getPlayer().canUpCart = false;
                                gameState.getPlayer().canRightCart = false;
                            }
                            break;
                    }
                }
            }
        }


        // Loop through all enemies to detect hit detection
        ArrayList<Enemy> enemies = gameState.getEnemies();
        synchronized (enemies) {

            for (Iterator<Enemy> itr = enemies.iterator(); itr.hasNext(); ) {
                PhysicsObject e = itr.next();
                //Attack Collision
                //if player is light attacking
                if (gameState.getPlayer().getCurrentAction() == Action.LIGHT) {
                    if (CollisionDetection.checkCollision(gameState.getPlayer().getAttackHitbox().getBoundsInParent(), e.getBounds().getBoundsInParent())) {
                        // e takes damage
                        // this will have to change due to Player being other controlled player when Enemy is when the player is an ai
                        Enemy enemy = (Enemy) e;
                        // TODO: For now its takes 3 damage, change later
                        enemy.removeHealth(3);
                        gameState.getPlayer().setCurrentAction(Action.IDLE);
                        System.out.println("hit");
                        // Sends to server
                    }

                }
                if (gameState.getPlayer().getCurrentAction() == Action.HEAVY) {
                    if (CollisionDetection.checkCollision(gameState.getPlayer().getHeavyAttackHitbox().getBoundsInParent(), e.getBounds().getBoundsInParent())) {
                        // e takes damage
                        Enemy enemy = (Enemy) e;
                        // TODO: For now its takes 10 damage, change later
                        enemy.removeHealth(10);
                        gameState.getPlayer().setCurrentAction(Action.IDLE);
                        System.out.println("heavy hit");
                        // Sends to server
                    }
                }
            }

        }
    }
}


