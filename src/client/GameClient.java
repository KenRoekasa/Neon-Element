package client;


import client.audiomanager.AudioManager;
import engine.Physics;
import engine.controller.RespawnController;
import graphics.debugger.Debugger;
import graphics.rendering.Renderer;
import graphics.userInterface.controllers.GameOverController;
import graphics.userInterface.controllers.PauseController;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import server.controllers.PowerUpController;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;

public class GameClient {

    /**
     * Time since the last frame
     */
    public static float deltaTime;

    private Physics physicsEngine;
    private Renderer renderer;
    private Debugger debugger;
    private GraphicsContext gc;
    private Stage primaryStage;
    private Scene scene;
    private Rectangle stageSize;
    private ArrayList<String> input;
    private ClientGameState gameState;
    private ClientNetworkThread clientNetworkThread;
    private Pane hudPane;

    private AudioManager audioManager;

    private GameClient(Stage primaryStage, ClientGameState gameState, boolean online) throws Exception {
        // initial setup
        this.primaryStage = primaryStage;
        this.gameState = gameState;

        // load hud
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../graphics/userInterface/fxmls/game_board.fxml"));
        //Pane hudPane = new Pane();

        try {
            hudPane = loader.load();
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

        renderer = new Renderer(gc, stageSize, debugger);

        audioManager = new AudioManager();

        //Creates the physics engine
        physicsEngine = new Physics(gameState);

        if (!online) {
            this.gameState.start();
            beginClientLoop(renderer);
        }

        // this.ClientNetworkThread = new ClientNetworkThread(gameState);
        // ClientNetworkThread.run();
    }

    /**
     * Local Game.
     */
    public GameClient(Stage primaryStage, ClientGameState gameState) throws Exception {
        this(primaryStage, gameState, false);
    }


    /**
     * Networked Game.
     */
    public GameClient(Stage primaryStage, ClientGameState gameState, String addr) throws Exception {
        this(primaryStage, gameState, true);

        this.clientNetworkThread = new ClientNetworkThread(gameState, InetAddress.getByName(addr));
    }

    public Scene getScene() {
        return scene;
    }

    private void beginClientLoop(Renderer renderer) {

        // initialise input controls
        initialiseInput(scene, renderer);

        new AnimationTimer() {
            long lastTime = System.nanoTime();
            public void handle(long currentNanoTime) {
                InputHandler.handleKeyboardInput(gameState.getPlayer(), input, gameState.getMap(), primaryStage);
                renderer.render(primaryStage, gameState);

                // TODO: remove this when networking is added
                physicsEngine.clientLoop();
                audioManager.clientLoop(gameState);

                //calculate deltaTime
                long time = System.nanoTime();
                deltaTime = (int) ((time - lastTime) / 1000000);
                lastTime = time;

                if (!gameState.getRunning()) {
                    stop();

                    showGameOver();
                }

            }


        }.start();

        // todo move to server
        Thread puController = new Thread(new PowerUpController(gameState));
        puController.start();
        // Respawn Controller
        Thread respawnController = new Thread(new RespawnController(gameState));
        respawnController.start();

    }


    private void showGameOver() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../graphics/userInterface/fxmls/gameover.fxml"));
        try {
            Pane root = loader.load();
            primaryStage.getScene().setRoot(root);
            root.setPrefHeight(stageSize.getHeight());
            root.setPrefWidth(stageSize.getWidth());
            GameOverController controller = loader.getController();
            controller.setStage(primaryStage);

            primaryStage.getScene().setCursor(Cursor.DEFAULT);

            primaryStage.setTitle("Game Over!");
            gameState.stop();

                    } catch (IOException e) {
                        System.out.println("crush in loading menu board ");
                        e.printStackTrace();
                    }
                }

    public void startNetwork() {
        this.clientNetworkThread.start();
        // TODO lobby screen

        // Wait for game to start
        while (!this.gameState.getRunning()) {
            try {
                Thread.sleep(1000L); // Every 1 second
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        beginClientLoop(renderer);
    }

    private void initialiseInput(Scene theScene, Renderer renderer) {
        // set input controls
        input = new ArrayList<>();

        theScene.setOnKeyReleased(e -> {
            String code = e.getCode().toString();
            input.remove(code);
        });

        theScene.setOnMouseClicked(e -> InputHandler.handleClick(gameState.getPlayer(), e));

        // when the mouse is moved around the screen calculate new angle
        theScene.setOnMouseMoved(e -> InputHandler.mouseAngleCalc(gameState.getPlayer(), primaryStage, e));
        theScene.setOnMouseDragged(e -> InputHandler.mouseAngleCalc(gameState.getPlayer(), primaryStage, e));

        theScene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.P) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("../graphics/userInterface/fxmls/pause.fxml"));
                try {
                    Pane node = loader.load();
                    node.setPrefHeight(stageSize.getHeight());
                    node.setPrefWidth(stageSize.getWidth());
                    hudPane.getChildren().add(node);
                    node.setBackground(Background.EMPTY);
                    PauseController controller = loader.getController();
                    controller.setHudPane(hudPane);
                    controller.setNode(node);
                    controller.setStageSize(stageSize);
                    controller.setStage(primaryStage, gameState);
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

    private void swapElement() {
        InputHandler.handleKeyboardInput(gameState.getPlayer(), input, gameState.getMap(), primaryStage);

    }
}
