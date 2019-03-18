package client;


import client.audiomanager.AudioManager;
import engine.physics.PhysicsController;
import engine.controller.RespawnController;
import graphics.debugger.Debugger;
import graphics.rendering.Renderer;
import graphics.userInterface.controllers.GameOverController;
import graphics.userInterface.controllers.HUDController;
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

    /**
     * The physics engine that runs in this current game/match
     */
    private PhysicsController physicsEngine;
    /**
     * The renderer that renders the this game/match
     */
    private Renderer renderer;
    /**
     * The debugger that allows text to be displayed on screen for debugging
     */
    private Debugger debugger;
    private GraphicsContext gc;
    private Stage primaryStage;
    private Scene scene;
    private Rectangle stageSize;
    /**
     * The array list of inputs that the user has inputted
     */
    private ArrayList<String> input;
    /**
     * The game state of the current game
     */
    private ClientGameState gameState;
    /**
     * The thread that connects to the server and controls what it sends and recieves
     */
    private ClientNetworkThread clientNetworkThread;

    private Pane hudPane;

    private AudioManager audioManager;


    /**
     * Constructor
     *
     * @param primaryStage the stage at which the game is rendered on
     * @param gameState    the game state of this current game/match
     * @param online       if the current game is online or offline
     * @param audioManager the audio manager that controls sounds in this current match/game
     */
    public GameClient(Stage primaryStage, ClientGameState gameState, boolean online, AudioManager audioManager) {
        // initial setup
        this.primaryStage = primaryStage;
        this.gameState = gameState;
        this.audioManager = audioManager;
        
        if(online) {
        	 FXMLLoader loader = new FXMLLoader(getClass().getResource("../graphics/userInterface/fxmls/hud.fxml"));
        }

        // load hud
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../graphics/userInterface/fxmls/hud.fxml"));
        //Pane hudPane = new Pane();

        try {
            hudPane = loader.load();
            //get player attribute

        } catch (Exception e) {
            // todo make this better
            System.out.println("Crush in loading hud in map");
            e.printStackTrace();
            Platform.exit();
            System.exit(0);
        }

        HUDController hudController = loader.getController();
        hudController.setGameState(gameState);
        hudController.setScoreBoard(gameState.getScoreBoard());
        hudController.setLeaderBoard(gameState.getScoreBoard().getLeaderBoard());
        hudController.setPlayerId(gameState.getPlayer().getId());
        hudController.setNum_player(gameState.getScoreBoard().getLeaderBoard().size());
        hudController.setAudioManager(audioManager);

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
        physicsEngine = new PhysicsController(gameState);

        // initialise input controls
        initialiseInput(scene, renderer);

        if (!online) {
            this.gameState.start();
            beginClientLoop(renderer, hudController);
        }

        // this.ClientNetworkThread = new ClientNetworkThread(gameState);
        // ClientNetworkThread.run();
    }

    /**
     * Local Game.
     */
    public GameClient(Stage primaryStage, ClientGameState gameState, AudioManager audioManager) throws Exception {
        this(primaryStage, gameState, false, audioManager);
    }


    /**
     * Networked Game.
     */
    public GameClient(Stage primaryStage, ClientGameState gameState, String addr, AudioManager audioManager) throws Exception {
        this(primaryStage, gameState, true, audioManager);

        this.clientNetworkThread = new ClientNetworkThread(gameState, InetAddress.getByName(addr));
    }

    public Scene getScene() {
        return scene;
    }

    /**
     * Start a loop controls the game
     *
     * @param renderer      the renderer
     * @param hudController the hud
     */
    private void beginClientLoop(Renderer renderer, HUDController hudController) {

        // initialise input controls
        initialiseInput(scene, renderer);

        new AnimationTimer() {
            long lastTime = System.nanoTime();

            public void handle(long currentNanoTime) {
                InputHandler.handleKeyboardInput(gameState.getPlayer(), input, gameState.getMap().getGround(), primaryStage);
                renderer.render(primaryStage, gameState);
                hudController.update();

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


    /**
     * Called when the game ends to swjitch to the game over screen
     */
    private void showGameOver() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../graphics/userInterface/fxmls/gameover.fxml"));
        try {
            Pane root = loader.load();
            primaryStage.getScene().setRoot(root);
            root.setPrefHeight(stageSize.getHeight());
            root.setPrefWidth(stageSize.getWidth());
            GameOverController controller = loader.getController();
            controller.setStage(primaryStage);
            controller.setAudioManager(audioManager);

            primaryStage.getScene().setCursor(Cursor.DEFAULT);

            primaryStage.setTitle("Game Over!");
            gameState.stop();

                    } catch (IOException e) {
                        System.out.println("crush in loading menu board ");
                        e.printStackTrace();
                    }
                }

    /**
     * Start a connection to the server
     */
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

        beginClientLoop(renderer, new HUDController());
    }

    private void initialiseInput(Scene theScene, Renderer renderer) {
        // set input controls
        input = new ArrayList<>();

        gameState.resume();

        theScene.setOnKeyReleased(e -> {
            if (!gameState.getPaused()) {
                String code = e.getCode().toString();
                input.remove(code);
            }
        });

        theScene.setOnMouseClicked(e -> {
            if (!gameState.getPaused()) {
                InputHandler.handleClick(gameState.getPlayer(), e);
            }
        });

        // when the mouse is moved around the screen calculate new angle
        theScene.setOnMouseMoved(e -> {
            if (!gameState.getPaused()) {
                InputHandler.mouseAngleCalc(gameState.getPlayer(), primaryStage, e);
            }
        });
        theScene.setOnMouseDragged(e -> {
            if (!gameState.getPaused()) {
                InputHandler.mouseAngleCalc(gameState.getPlayer(), primaryStage, e);
            }
        });

        theScene.setOnKeyPressed(e -> {
            if (!gameState.getPaused()) {
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
                        controller.setAudioManager(audioManager);
                        gameState.pause();
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
            }
        });
    }

}
