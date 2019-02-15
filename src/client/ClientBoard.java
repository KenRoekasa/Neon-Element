package client;

import controllers.HUDController;
import calculations.DamageCalculation;
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

import java.util.ArrayList;
import java.util.Iterator;

public class ClientBoard {

    private Debugger debugger;
    private GraphicsContext gc;
    private Stage primaryStage;
    private Scene scene;
    private Rectangle stageSize;
    private ArrayList<String> input;

    private ClientGameState gameState;
    private GameClient gameClient;

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
            HUDController HUDController = loader.getController();
            HUDController.initPlayer(gameState.getPlayer());
        } catch (Exception e) {
            // todo make this better
            System.out.println("Crash in loading hud in map");
            e.printStackTrace();
            Platform.exit();
            System.exit(0);
        }

        primaryStage.getScene().setRoot(hudPane);
        HUDController controller =loader.getController();
        System.out.println("HUDloader: "+controller.hashCode());
        controller.setStage(primaryStage);
        //controller.

        scene = primaryStage.getScene();

        stageSize = new Rectangle(primaryStage.getWidth(), primaryStage.getHeight());

        Canvas canvas = new Canvas(stageSize.getWidth(), stageSize.getHeight());
        hudPane.getChildren().add(canvas);

        // forces the game to be rendered behind the gui
        int index = hudPane.getChildren().indexOf(canvas);
        hudPane.getChildren().get(index).toBack();

        gc = canvas.getGraphicsContext2D();
        debugger = new Debugger(gc);

        Renderer renderer = new Renderer(gc, stageSize, debugger);

        // initialise input controls
        initialiseInput(scene, renderer);

        beginClientLoop(renderer);

        // this.gameClient = new GameClient(gameState);
        // gameClient.run();
    }

    public Scene getScene() {
        return scene;
    }

    private void beginClientLoop(Renderer renderer) {
        new AnimationTimer() {
            public void handle(long currentNanoTime) {
                InputHandler.handleKeyboardInput(gameState.getPlayer(), input, gameState.getMap());
                renderer.render(primaryStage, gameState);
                // TODO: remove this when networking is added
                clientLoop();

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

        theScene.setOnKeyReleased(e -> {
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
            // Call update function for all physics objects
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
            Point2D previousLocation = gameState.getPlayer().getLocation();
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

                    int collisionOffset = 1;
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
//                    System.out.println(previousLocation);
                    // Check for rare occasion the player is inside another player
                    if (CollisionDetection.checkCollision(gameState.getPlayer(), e)) {
                        // This line of code seems to cause a bug
                        gameState.getPlayer().setLocation(previousLocation);
                        System.out.println("colliding");
                    }

                    switch (gameState.getPlayer().getCharacterDirection()) {
                        case UP:
                            projectedPlayer.setLocation(checkUp);
                            if (CollisionDetection.checkCollision(projectedPlayer, e)) {
                                Point2D newLocation = previousLocation;
                                // if on the right hand side of the other player
                                if (e.getBounds().getBoundsInParent().getMaxX() <= gameState.getPlayer().getLocation()
                                        .getX()) {
                                    double adjacent = gameState.getPlayer().getLocation().getX()
                                            - e.getBounds().getBoundsInParent().getMaxX();
                                    double opposite = (adjacent * Math.tan(Math.toRadians(45)));
                                    newLocation = new Point2D(e.getBounds().getBoundsInParent().getMaxX(),
                                            gameState.getPlayer().getLocation().getY() - opposite);
                                    newLocation = newLocation.add(collisionOffset, collisionOffset);
                                }
                                // if on the left hand side of the other player
                                if (e.getBounds().getBoundsInParent().getMaxX() > gameState.getPlayer().getLocation()
                                        .getX()) {
                                    double adjacent = e.getBounds().getBoundsInParent().getMaxY()
                                            - gameState.getPlayer().getLocation().getY();
                                    double opposite = (adjacent * Math.tan(Math.toRadians(45)));
                                    newLocation = new Point2D(gameState.getPlayer().getLocation().getX() - opposite,
                                            e.getBounds().getBoundsInParent().getMaxY());
                                    newLocation = newLocation.add(-collisionOffset, collisionOffset);

                                }

                                previousLocation = newLocation;
                                gameState.getPlayer().setLocation(newLocation);
                                gameState.getPlayer().canUp = false;
                                gameState.getPlayer().canUpCart = false;
                                gameState.getPlayer().canLeftCart = false;
                            }

                            break;
                        case DOWN:
                            projectedPlayer.setLocation(checkDown);
                            if ((CollisionDetection.checkCollision(projectedPlayer, e))) {
                                Point2D newLocation = previousLocation;
                                // if on the right hand side of the other player
                                if (gameState.getPlayer().getBounds().getBoundsInParent().getMaxY() <= e.getBounds()
                                        .getBoundsInParent().getMinY()) {
                                    double adjacent = e.getBounds().getBoundsInParent().getMinY()
                                            - gameState.getPlayer().getBounds().getBoundsInParent().getMaxY();
                                    double opposite = (adjacent * Math.tan(Math.toRadians(45)));
                                    newLocation = new Point2D(gameState.getPlayer().getLocation().getX() + opposite,
                                            gameState.getPlayer().getLocation().getY() + adjacent);
                                    newLocation = newLocation.add(-collisionOffset, -collisionOffset);
                                }

                                // if on the left hand side of the other player
                                if (gameState.getPlayer().getBounds().getBoundsInParent().getMaxY() > e.getBounds()
                                        .getBoundsInParent().getMinY()) {
                                    double adjacent = e.getBounds().getBoundsInParent().getMinX()
                                            - gameState.getPlayer().getBounds().getBoundsInParent().getMaxX();
                                    double opposite = (adjacent * Math.tan(Math.toRadians(45)));
                                    newLocation = new Point2D(gameState.getPlayer().getLocation().getX() + adjacent,
                                            gameState.getPlayer().getLocation().getY() + opposite);
                                    newLocation = newLocation.add(-collisionOffset, -collisionOffset);

                                }

                                previousLocation = newLocation;
                                gameState.getPlayer().setLocation(newLocation);
                                gameState.getPlayer().canDown = false;
                                gameState.getPlayer().canDownCart = false;
                                gameState.getPlayer().canRightCart = false;
                            }
                            break;
                        case LEFT:
                            projectedPlayer.setLocation(checkLeft);
                            if ((CollisionDetection.checkCollision(projectedPlayer, e))) {
                                Point2D newLocation = gameState.getPlayer().getLocation();
                                // if above the other player
                                System.out.println(gameState.getPlayer().getBounds().getBoundsInParent().getMaxY() +", "+ e.getLocation()
                                        .getY());
                                if (gameState.getPlayer().getBounds().getBoundsInParent().getMaxY() <= e.getLocation()
                                        .getY()) {

                                    double adjacent = e.getBounds().getBoundsInParent().getMinY()
                                            - gameState.getPlayer().getBounds().getBoundsInParent().getMaxY();
                                    double opposite = (adjacent * Math.tan(Math.toRadians(45)));
                                    newLocation = new Point2D(gameState.getPlayer().getLocation().getX() - opposite,
                                            gameState.getPlayer().getLocation().getY() + adjacent);
                                    newLocation = newLocation.add(collisionOffset, -collisionOffset);
                                    System.out.println("Moving Left, and is above the other player, newLocation: " + newLocation);
                                }

                                // if below the other player
                                if (gameState.getPlayer().getBounds().getBoundsInParent().getMaxY() > e.getLocation()
                                        .getY()) {

                                    double opposite = gameState.getPlayer().getLocation().getX()
                                            - e.getBounds().getBoundsInParent().getMaxX();
                                    double adjacent = (opposite * Math.tan(Math.toRadians(45)));
                                    newLocation = new Point2D(gameState.getPlayer().getLocation().getX() - opposite,
                                            gameState.getPlayer().getLocation().getY() + adjacent);
                                    newLocation = newLocation.add(collisionOffset, -collisionOffset);
                                    System.out.println("Moving Left, and is below the other player, newLocation: " + newLocation);
                                }
                                previousLocation = newLocation;
                                gameState.getPlayer().setLocation(newLocation);
                                gameState.getPlayer().canLeft = false;
                                gameState.getPlayer().canDownCart = false;
                                gameState.getPlayer().canLeftCart = false;
                                gameState.getPlayer().canRightCart = false;

                            }
                            break;
                        case RIGHT:
                            projectedPlayer.setLocation(checkRight);
                            if ((CollisionDetection.checkCollision(projectedPlayer, e))) {
                                // test every the most amount of movement before it collides
                                Point2D newLocation = gameState.getPlayer().getLocation();
                                // if above the other player
                                if (gameState.getPlayer().getBounds().getBoundsInParent().getMaxX() <= e.getLocation()
                                        .getX()) {
                                    double adjacent = e.getLocation().getX()
                                            - gameState.getPlayer().getBounds().getBoundsInParent().getMaxX();
                                    double opposite = (adjacent * Math.tan(Math.toRadians(45)));
                                    newLocation = new Point2D(gameState.getPlayer().getLocation().getX() + adjacent,
                                            gameState.getPlayer().getLocation().getY() - opposite);
                                    newLocation = newLocation.add(-collisionOffset, collisionOffset);

                                }

                                // if below the other player
                                if (gameState.getPlayer().getBounds().getBoundsInParent().getMaxX() > e.getLocation()
                                        .getX()) {
                                    double opposite = gameState.getPlayer().getLocation().getY()
                                            - e.getBounds().getBoundsInParent().getMaxY();
                                    double adjacent = (opposite * Math.tan(Math.toRadians(45)));
                                    newLocation = new Point2D(gameState.getPlayer().getLocation().getX() + adjacent,
                                            gameState.getPlayer().getLocation().getY() - opposite);
                                    newLocation = newLocation.add(-collisionOffset, collisionOffset);
                                }
                                previousLocation = newLocation;
                                gameState.getPlayer().setLocation(newLocation);
                                gameState.getPlayer().canRight = false;
                                gameState.getPlayer().canDown = false;
                                gameState.getPlayer().canUpCart = false;
                                gameState.getPlayer().canRightCart = false;
                            }
                            break;
                        case UPCART:
                            projectedPlayer.setLocation(checkUpCart);
                            if ((CollisionDetection.checkCollision(projectedPlayer, e))) {
                                Point2D newLocation = new Point2D(gameState.getPlayer().getLocation().getX(),
                                        e.getBounds().getBoundsInParent().getMaxY()+collisionOffset);
                                previousLocation = newLocation;
                                gameState.getPlayer().setLocation(newLocation);
                                gameState.getPlayer().canUpCart = false;
                                gameState.getPlayer().canUp = false;
                                gameState.getPlayer().canRight = false;
                                gameState.getPlayer().canLeftCart = false;
                            }
                            break;
                        case DOWNCART:
                            projectedPlayer.setLocation(checkDownCart);
                            if ((CollisionDetection.checkCollision(projectedPlayer, e))) {
                                // test every the most amount of movement before it collides
                                Point2D newLocation = new Point2D(gameState.getPlayer().getLocation().getX(),
                                        e.getBounds().getBoundsInParent().getMinY() - gameState.getPlayer().getWidth() -collisionOffset);
                                previousLocation = newLocation;
                                gameState.getPlayer().setLocation(newLocation);
                                gameState.getPlayer().canDownCart = false;
                                gameState.getPlayer().canDown = false;
                                gameState.getPlayer().canLeft = false;
                                gameState.getPlayer().canRightCart = false;
                            }
                            break;
                        case LEFTCART:
                            projectedPlayer.setLocation(checkLeftCart);
                            if ((CollisionDetection.checkCollision(projectedPlayer, e))) {
                                //
                                Point2D newLocation = new Point2D(e.getBounds().getBoundsInParent().getMaxX() + collisionOffset,
                                        gameState.getPlayer().getLocation().getY());
                                ;
                                previousLocation = newLocation;
                                gameState.getPlayer().setLocation(newLocation);
                                gameState.getPlayer().canLeftCart = false;
                                gameState.getPlayer().canUp = false;
                                gameState.getPlayer().canLeft = false;
                                gameState.getPlayer().canUpCart = false;
                            }
                            break;
                        case RIGHTCART:
                            projectedPlayer.setLocation(checkRightCart);
                            if ((CollisionDetection.checkCollision(projectedPlayer, e))) {
                                // test every the most amount of movement before it collides
                                Point2D newLocation = new Point2D(
                                        e.getBounds().getBoundsInParent().getMinX() - gameState.getPlayer().getWidth()-collisionOffset,
                                        gameState.getPlayer().getLocation().getY());

                                previousLocation = newLocation;
                                gameState.getPlayer().setLocation(newLocation);
                                gameState.getPlayer().canRightCart = false;
                                gameState.getPlayer().canDown = false;
                                gameState.getPlayer().canRight = false;
                                gameState.getPlayer().canDownCart = false;
                                gameState.getPlayer().canLeft = false;
                            }
                            break;
                    }
                }
            }
        }


        //temporary for enemies to pickup power ups
        //TODO: CHange this
        for (Iterator<PhysicsObject> itr = objects.iterator(); itr.hasNext(); ) {
            PhysicsObject e = itr.next();
            // Check if the moving in a certain direction will cause a collision
            // The player has collided with e do something
            if (e.getTag() == ObjectType.POWERUP) {
                if (CollisionDetection.checkCollision(gameState.getEnemies().get(0), e)) {
                    PowerUp powerUp = (PowerUp) e;
                    powerUp.activatePowerUp(gameState.getEnemies().get(0));
                    // remove power up from objects array list
                    itr.remove();
                }
            }
        }

        // Loop through all enemies to detect hit detection
        ArrayList<Enemy> enemies = gameState.getEnemies();
        synchronized (enemies) {
            for (Iterator<Enemy> itr = enemies.iterator(); itr.hasNext(); ) {
                PhysicsObject e = itr.next();
                // Attack Collision
                // if player is light attacking
                if (gameState.getPlayer().getCurrentAction() == Action.LIGHT) {
                    if (CollisionDetection.checkCollision(gameState.getPlayer().getAttackHitbox().getBoundsInParent(),
                            e.getBounds().getBoundsInParent())) {
                        // e takes damage
                        // this will have to change due to Player being other controlled player when
                        // Enemy is when the player is an ai
                        Enemy enemy = (Enemy) e;
                        enemy.removeHealth(DamageCalculation.calculateDealtDamage(gameState.getPlayer(), enemy));
                        gameState.getPlayer().setCurrentAction(Action.IDLE);
                        System.out.println("hit");
                        // Sends to server
                    }

                }
                if (gameState.getPlayer().getCurrentAction() == Action.HEAVY) {
                    if (CollisionDetection.checkCollision(
                            gameState.getPlayer().getHeavyAttackHitbox().getBoundsInParent(),
                            e.getBounds().getBoundsInParent())) {
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
