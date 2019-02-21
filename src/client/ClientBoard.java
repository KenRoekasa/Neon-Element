package client;


import engine.ScoreBoard;
import engine.calculations.DamageCalculation;
import engine.controller.RespawnController;
import engine.entities.CollisionDetection;
import engine.entities.PhysicsObject;
import engine.entities.Player;
import engine.entities.PowerUp;
import engine.enums.Action;
import engine.enums.ObjectType;
import graphics.debugger.Debugger;
import graphics.rendering.Renderer;
import graphics.userInterface.controllers.HUDController;
import graphics.userInterface.controllers.PauseController;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.ImageCursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import server.controllers.PowerUpController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.LinkedBlockingQueue;

public class ClientBoard {

    private Debugger debugger;
    private GraphicsContext gc;
    private Stage primaryStage;
    private Scene scene;
    private Rectangle stageSize;
    private ArrayList<String> input;

    private ClientGameState gameState;
    private GameClient gameClient;
    private Pane hudPane;



    public ClientBoard(Stage primaryStage, ClientGameState gameState) throws Exception {
        // initial setup
        this.primaryStage = primaryStage;
        this.gameState = gameState;
        this.gameClient = new GameClient(gameState);
        // load hud
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../graphics/userInterface/fxmls/game_board.fxml"));
        //Pane hudPane = new Pane();

        try {
            hudPane = (Pane) loader.load();
            //get player attribute

        } catch (Exception e) {
            // todo make this better
            System.out.println("Crash in loading hud in map");
            e.printStackTrace();
            Platform.exit();
            System.exit(0);
        }

        primaryStage.getScene().setRoot(hudPane);

        scene = primaryStage.getScene();

		// change cursor
        Image cursorImage = new Image("graphics/rendering/textures/cursor.png");
        ImageCursor iC = new ImageCursor(cursorImage, cursorImage.getWidth() / 2, cursorImage.getHeight() / 2);
        scene.setCursor(iC);

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
                InputHandler.handleKeyboardInput(gameState.getPlayer(), input, gameState.getMap(),primaryStage);
                renderer.render(primaryStage, gameState);
                // TODO: remove this when networking is added
                clientLoop();

            }
        }.start();
        Thread puController = new Thread(new PowerUpController(gameState.getObjects()));
        puController.start();
        // Respawn Controller
        Thread respawnController = new Thread(new RespawnController(gameState));
        respawnController.start();
    }

    public void startGame() {
        this.gameClient.start();
    }

    private void initialiseInput(Scene theScene, Renderer renderer) {
        // set input controls
        input = new ArrayList<>();

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

        theScene.setOnKeyPressed(e -> {
            if(e.getCode() == KeyCode.P) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("../graphics/userInterface/fxmls/pause.fxml"));
                try {
                    Pane node = loader.load();
                    node.setPrefHeight(stageSize.getHeight());
                    node.setPrefWidth(stageSize.getWidth());
                    hudPane.getChildren().add(node);
                    node.setBackground(Background.EMPTY);
                    PauseController controller = loader.getController();
                    controller.setStage(primaryStage);
                    primaryStage.setTitle("Pause");

                } catch (IOException ex) {
                    System.out.println("crush in loading pause board ");
                    ex.printStackTrace();
                }
            }

            String code = e.getCode().toString();
            // only add each input command once
            if (!input.contains(code))
                input.add(code);
        });
    }

    private void clientLoop() {
        InputHandler.handleKeyboardInput(gameState.getPlayer(), input, gameState.getMap(),primaryStage);
        doCollisionDetection();
        doHitDetection();
        doUpdates();
        deathHandler();

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


    private void deathHandler() {
        ArrayList<Player> allPlayers = gameState.getAllPlayers();
        LinkedBlockingQueue deadPlayers = gameState.getDeadPlayers();
        ScoreBoard scoreBoard = gameState.getScoreBoard();
        synchronized (deadPlayers) {
            for (Iterator<Player> itr = allPlayers.iterator(); itr.hasNext(); ) {
                Player player = itr.next();
                //If not already dead
                if (!deadPlayers.contains(player)) {
                    if (!player.isAlive()) {
                        // Add to dead list
                        deadPlayers.offer(player);
                        // Add kills to scoreboard
                        scoreBoard.addKill(player.getLastAttacker().getId(), player.getId());
                        //if dead teleport player off screen
                        player.setLocation(new Point2D(5000, 5000));

                    }
                }
            }


        }
    }


    private void swapElement(){
        InputHandler.handleKeyboardInput(gameState.getPlayer(),input,gameState.getMap(),primaryStage);

    }


    private void doCollisionDetection() {

        ArrayList<PhysicsObject> objects = gameState.getObjects();
        ArrayList<Player> allPlayers = gameState.getAllPlayers();

        for (Iterator<Player> itr = allPlayers.iterator(); itr.hasNext(); ) {
            Player player = itr.next();
            ArrayList<Player> otherPlayers = gameState.getOtherPlayers(player);
            synchronized (objects) {
                // Collision detection code
                player.canUp = true;
                player.canLeft = true;
                player.canRight = true;
                player.canDown = true;
                player.canUpCart = true;
                player.canRightCart = true;
                player.canLeftCart = true;
                player.canDownCart = true;
                Point2D previousLocation = player.getLocation();
                Player projectedPlayer = new Player(ObjectType.PLAYER);

                ArrayList<PhysicsObject> otherObjects = gameState.getOtherObjects(player);
                for (Iterator<PhysicsObject> itr1 = otherObjects.iterator(); itr1.hasNext(); ) {
                    PhysicsObject e = itr1.next();
                    // Check if the moving in a certain direction will cause a collision
                    // The player has collided with e do something
                    if (e.getTag() == ObjectType.POWERUP) {
                        if (CollisionDetection.checkCollision(player, e)) {
                            PowerUp powerUp = (PowerUp) e;
                            powerUp.activatePowerUp(player);
                            // remove power up from objects array list
                            itr1.remove();
                            objects.remove(e);
                        }
                    } else {

                        int collisionOffset = 1;
                        double x = player.getLocation().getX();
                        double y = player.getLocation().getY();
                        int movementSpeed = player.getMovementSpeed();
                        Point2D checkUp = new Point2D(x - movementSpeed, y - movementSpeed);
                        Point2D checkDown = new Point2D(x + movementSpeed, y + movementSpeed);
                        Point2D checkLeft = new Point2D(x - movementSpeed, y + movementSpeed);
                        Point2D checkRight = new Point2D(x + movementSpeed, y - movementSpeed);
                        Point2D checkUpCart = new Point2D(x, y - movementSpeed);
                        Point2D checkDownCart = new Point2D(x, y + movementSpeed);
                        Point2D checkLeftCart = new Point2D(x - movementSpeed, y);
                        Point2D checkRightCart = new Point2D(x + movementSpeed, y);

                        // Check for rare occasion the player is inside another player
                        if (CollisionDetection.checkCollision(player, e)) {
                            // This line of code seems to cause a bug
                            //                        gameState.getPlayer().setLocation(previousLocation);
                            if (player == e) {
                                //                                System.out.println(player + " Collided with " + e);
                                //                                System.out.println("Collided with itself");
                            }
                        }

                        switch (player.getCharacterDirection()) {
                            case UP:
                                projectedPlayer.setLocation(checkUp);

                                if (CollisionDetection.checkCollision(projectedPlayer, e)) {
                                    Point2D newLocation = previousLocation;
                                    // if on the right hand side of the other player
                                    if (e.getBounds().getBoundsInParent().getMaxX() <= player.getLocation().getX() && e.getBounds().getBoundsInParent().getMaxY() >= player.getLocation().getY()) {
                                        double adjacent = player.getLocation().getX()
                                                - e.getBounds().getBoundsInParent().getMaxX();
                                        double opposite = (adjacent * Math.tan(Math.toRadians(45)));
                                        newLocation = new Point2D(e.getBounds().getBoundsInParent().getMaxX(),
                                                player.getLocation().getY() - opposite);
                                        newLocation = newLocation.add(collisionOffset, collisionOffset);
                                    }
                                    // if on the left hand side of the other player
                                    if (e.getBounds().getBoundsInParent().getMaxX() > player.getLocation().getX() && e.getBounds().getBoundsInParent().getMaxY() < player.getLocation().getY()) {
                                        double adjacent = e.getBounds().getBoundsInParent().getMaxY()
                                                - player.getLocation().getY();
                                        double opposite = (adjacent * Math.tan(Math.toRadians(45)));
                                        newLocation = new Point2D(player.getLocation().getX() - opposite,
                                                e.getBounds().getBoundsInParent().getMaxY());
                                        newLocation = newLocation.add(-collisionOffset, collisionOffset);

                                    }

                                    previousLocation = newLocation;
                                    player.setLocation(newLocation);
                                    player.canUp = false;
                                    player.canUpCart = false;
                                    player.canLeftCart = false;
                                }

                                break;
                            case DOWN:
                                projectedPlayer.setLocation(checkDown);
                                if ((CollisionDetection.checkCollision(projectedPlayer, e))) {
                                    Point2D newLocation = previousLocation;
                                    // if on the right hand side of the other player
                                    if (player.getBounds().getBoundsInParent().getMaxY() <= e.getBounds()
                                            .getBoundsInParent().getMinY() && player.getBounds().getBoundsInParent().getMaxX() <= e.getBounds()
                                            .getBoundsInParent().getMinX()) {
                                        double adjacent = e.getBounds().getBoundsInParent().getMinY()
                                                - player.getBounds().getBoundsInParent().getMaxY();
                                        double opposite = (adjacent * Math.tan(Math.toRadians(45)));
                                        newLocation = new Point2D(player.getLocation().getX() + opposite,
                                                player.getLocation().getY() + adjacent);
                                        newLocation = newLocation.add(-collisionOffset, -collisionOffset);
                                    }

                                    // if on the left hand side of the other player
                                    if (player.getBounds().getBoundsInParent().getMaxY() > e.getBounds()
                                            .getBoundsInParent().getMinY() && player.getBounds().getBoundsInParent().getMaxX() > e.getBounds()
                                            .getBoundsInParent().getMinX()) {
                                        double adjacent = e.getBounds().getBoundsInParent().getMinX()
                                                - player.getBounds().getBoundsInParent().getMaxX();
                                        double opposite = (adjacent * Math.tan(Math.toRadians(45)));
                                        newLocation = new Point2D(player.getLocation().getX() + adjacent,
                                                player.getLocation().getY() + opposite);
                                        newLocation = newLocation.add(-collisionOffset, -collisionOffset);

                                    }

                                    previousLocation = newLocation;
                                    player.setLocation(newLocation);
                                    player.canDown = false;
                                    player.canDownCart = false;
                                    player.canRightCart = false;
                                }
                                break;
                            case LEFT:
                                projectedPlayer.setLocation(checkLeft);
                                if ((CollisionDetection.checkCollision(projectedPlayer, e))) {
                                    // test every the most amount of movement before it collides
                                    Point2D newLocation = player.getLocation();
                                    // if above the other player
                                    if (player.getBounds().getBoundsInParent().getMaxY() <= e.getLocation().getY() && player.getLocation().getX() >= e.getBounds().getBoundsInParent().getMaxX()) {
                                        double adjacent = e.getBounds().getBoundsInParent().getMinY()
                                                - player.getBounds().getBoundsInParent().getMaxY();
                                        double opposite = (adjacent * Math.tan(Math.toRadians(45)));
                                        newLocation = new Point2D(player.getLocation().getX() - opposite,
                                                player.getLocation().getY() + adjacent);
                                        newLocation = newLocation.add(collisionOffset, -collisionOffset);
                                    }

                                    // if below the other player
                                    if (player.getBounds().getBoundsInParent().getMaxY() > e.getLocation().getY() && player.getLocation().getX() < e.getBounds().getBoundsInParent().getMaxX()) {

                                        double opposite = player.getLocation().getX()
                                                - e.getBounds().getBoundsInParent().getMaxX();
                                        double adjacent = (opposite * Math.tan(Math.toRadians(45)));
                                        newLocation = new Point2D(player.getLocation().getX() - opposite,
                                                player.getLocation().getY() + adjacent);
                                        newLocation = newLocation.add(collisionOffset, -collisionOffset);
                                    }
                                    previousLocation = newLocation;
                                    player.setLocation(newLocation);
                                    player.canLeft = false;
                                    player.canDownCart = false;
                                    player.canLeftCart = false;
                                    player.canRightCart = false;

                                }
                                break;
                            case RIGHT:
                                projectedPlayer.setLocation(checkRight);
                                if ((CollisionDetection.checkCollision(projectedPlayer, e))) {
                                    // test every the most amount of movement before it collides
                                    Point2D newLocation = player.getLocation();
                                    // if above the other player
                                    if (player.getBounds().getBoundsInParent().getMaxX() <= e.getLocation().getX() && player.getBounds().getBoundsInParent().getMaxY() <= e.getLocation().getY() ) {
                                        double adjacent = e.getLocation().getX()
                                                - player.getBounds().getBoundsInParent().getMaxX();
                                        double opposite = (adjacent * Math.tan(Math.toRadians(45)));
                                        newLocation = new Point2D(player.getLocation().getX() + adjacent,
                                                player.getLocation().getY() - opposite);
                                        newLocation = newLocation.add(-collisionOffset, collisionOffset);

                                    }

                                    // if below the other player
                                    if (player.getBounds().getBoundsInParent().getMaxX() > e.getLocation().getX() && player.getBounds().getBoundsInParent().getMaxY() > e.getLocation().getY()) {
                                        double opposite = player.getLocation().getY()
                                                - e.getBounds().getBoundsInParent().getMaxY();
                                        double adjacent = (opposite * Math.tan(Math.toRadians(45)));
                                        newLocation = new Point2D(player.getLocation().getX() + adjacent,
                                                player.getLocation().getY() - opposite);
                                        newLocation = newLocation.add(-collisionOffset, collisionOffset);
                                    }
                                    previousLocation = newLocation;
                                    player.setLocation(newLocation);
                                    player.canRight = false;
                                    player.canDown = false;
                                    player.canUpCart = false;
                                    player.canRightCart = false;
                                }
                                break;
                            case UPCART:
                                projectedPlayer.setLocation(checkUpCart);
                                if ((CollisionDetection.checkCollision(projectedPlayer, e))) {
                                    // test every the most amount of movement before it collides
                                    Point2D newLocation = new Point2D(player.getLocation().getX(),
                                            e.getBounds().getBoundsInParent().getMaxY());
                                    previousLocation = newLocation;
                                    player.setLocation(newLocation);
                                    player.canUpCart = false;
                                    player.canUp = false;
                                    player.canRight = false;
                                    player.canLeftCart = false;
                                }
                                break;
                            case DOWNCART:
                                projectedPlayer.setLocation(checkDownCart);
                                if ((CollisionDetection.checkCollision(projectedPlayer, e))) {
                                    // test every the most amount of movement before it collides
                                    Point2D newLocation = new Point2D(player.getLocation().getX(),
                                            e.getBounds().getBoundsInParent().getMinY() - player.getWidth());
                                    previousLocation = newLocation;
                                    player.setLocation(newLocation);
                                    player.canDownCart = false;
                                    player.canDown = false;
                                    player.canLeft = false;
                                    player.canRightCart = false;
                                }
                                break;
                            case LEFTCART:
                                projectedPlayer.setLocation(checkLeftCart);
                                if ((CollisionDetection.checkCollision(projectedPlayer, e))) {
                                    //
                                    Point2D newLocation = new Point2D(e.getBounds().getBoundsInParent().getMaxX(),
                                            player.getLocation().getY());
                                    ;
                                    previousLocation = newLocation;
                                    player.setLocation(newLocation);
                                    player.canLeftCart = false;
                                    player.canUp = false;
                                    player.canLeft = false;
                                    player.canUpCart = false;
                                }
                                break;
                            case RIGHTCART:
                                projectedPlayer.setLocation(checkRightCart);
                                if ((CollisionDetection.checkCollision(projectedPlayer, e))) {
                                    // test every the most amount of movement before it collides
                                    Point2D newLocation = new Point2D(
                                            e.getBounds().getBoundsInParent().getMinX() - player.getWidth(),
                                            player.getLocation().getY());

                                    previousLocation = newLocation;
                                    player.setLocation(newLocation);
                                    player.canRightCart = false;
                                    player.canDown = false;
                                    player.canRight = false;
                                    player.canDownCart = false;
                                    player.canLeft = false;
                                }
                                break;
                        }
                    }
                }
            }


        }
    }

    private void doHitDetection() {
        ArrayList<Player> allPlayers = gameState.getAllPlayers();
        for (Iterator<Player> itr = allPlayers.iterator(); itr.hasNext(); ) {
            Player player = itr.next();
            ArrayList<Player> otherPlayers = gameState.getOtherPlayers(player);
            // Loop through all enemies to detect hit detection
            for (Iterator<Player> itr1 = otherPlayers.iterator(); itr1.hasNext(); ) {
                PhysicsObject e = itr1.next();
                // Attack Collision
                // if player is light attacking
                if (player.getCurrentAction() == Action.LIGHT) {
                    if (CollisionDetection.checkCollision(player.getAttackHitbox().getBoundsInParent(),
                            e.getBounds().getBoundsInParent())) {
                        // e takes damage
                        // this will have to change due to Player being other controlled player when
                        // Enemy is when the player is an ai
                        Player enemy = (Player) e;
                        enemy.removeHealth(DamageCalculation.calculateDealtDamage(player, enemy), enemy);
                        player.setCurrentAction(Action.IDLE);
                        //System.out.println("hit");
                        // Sends to server
                    }

                }
                if (player.getCurrentAction() == Action.HEAVY) {
                    if (CollisionDetection.checkCollision(player.getHeavyAttackHitbox().getBoundsInParent(),
                            e.getBounds().getBoundsInParent())) {
                        // e takes damage
                        Player enemy = (Player) e;
                        // TODO: For now its takes 10 damage, change later
                        enemy.removeHealth(DamageCalculation.calculateDealtDamage(player, enemy), enemy);
                        player.setCurrentAction(Action.IDLE);
                        //System.out.println("heavy hit");
                        // Sends to server
                    }
                }
            }
        }
    }

}
