package client;

import client.audiomanager.AudioManager;
import engine.physics.DeltaTime;
import engine.physics.PhysicsController;
import engine.controller.RespawnController;
import graphics.debugger.Debugger;
import graphics.rendering.Renderer;
import graphics.userInterface.controllers.*;
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
import engine.controller.PowerUpController;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;

public class GameClient {


    public static long timeElapsed;


    public static long pauseStart;

    public boolean isNetworked;

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

    private Pane hudPane,lobbyPane;

    private AudioManager audioManager;
    private Boolean tab;
    private Pane leaderboard;
    private LeaderboardController leaderboardController;

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
        this.isNetworked = online;

        // this.ClientNetworkThread = new ClientNetworkThread(gameState);
        // ClientNetworkThread.run();
    }
    /**
     * Local Game.
     */
    public GameClient(Stage primaryStage, ClientGameState gameState, AudioManager audioManager) throws Exception {
        this(primaryStage, gameState, false, audioManager);
        this.gameState.getScoreBoard().initialise(gameState.getAllPlayers());
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
        // Update methods for these are not called if isNetworked is true
        PowerUpController powerUpController = new PowerUpController(gameState);
        RespawnController respawnController = new RespawnController(gameState);

        // initialise input controls
        initialiseInput(scene, renderer);

        new AnimationTimer() {
            long lastTime = System.nanoTime();
            long pauseDuration = 0;
            public void handle(long currentNanoTime) {
                InputHandler.handleKeyboardInput(gameState.getPlayer(), input, gameState.getMap().getGround(), primaryStage);
                renderer.render(primaryStage, gameState);
                hudController.update();

                // TODO: remove this when networking is added
                if(gameState.getPaused()){
                    long now = System.nanoTime()/1000000;
                    pauseDuration = (now - pauseStart);
                    gameState.getAiConMan().pauseAllAi();
                   
                }



                if(!gameState.getPaused()){
                    timeElapsed += DeltaTime.deltaTime;

                    if(!isNetworked) {
                        //client loop with hit detection
                        physicsEngine.clientLoop();
                        powerUpController.update();
                        respawnController.update();

                    }else{
                        //client loop without hit detection
                        physicsEngine.dumbClientLoop();
                    }

                    pauseDuration = 0;
                    

                }
                audioManager.clientLoop(gameState);


                //calculate deltaTime
                long time = System.nanoTime();
                DeltaTime.deltaTime = (int) ((time - lastTime) / 1000000);
                lastTime = time;

                if (!gameState.getRunning()) {
                    stop();

                    showGameOver();
                }

            }


        }.start();

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
            gameState.stop();
            GameOverController controller = loader.getController();
            controller.setStage(primaryStage);
            controller.setAudioManager(audioManager);
            controller.setScoreBoard(gameState.getScoreBoard());
            controller.setLeaderBoard(gameState.getScoreBoard().getLeaderBoard());
            controller.setNum_players(gameState.getScoreBoard().getLeaderBoard().size());
            controller.showLeaderBoard();
            primaryStage.getScene().setCursor(Cursor.DEFAULT);
            primaryStage.setTitle("Game Over");
            audioManager.setMenuMusic();
            audioManager.setNeonVolume(audioManager.getEffectVolume());
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


    }

    private void initialiseInput(Scene theScene, Renderer renderer) {
        // set input controls
        input = new ArrayList<>();

        gameState.resume();

        tab = false;

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
                        pauseStart = System.nanoTime() / 1000000;
                        input.clear();
                        primaryStage.setTitle("Pause");
                    } catch (IOException ex) {
                        System.out.println("crush in loading pause board ");
                        ex.printStackTrace();
                    }
                } else if (e.getCode() == KeyCode.TAB) {
                    if (!tab) {
                        System.out.println("Tab show info");
                        leaderboard.setPrefHeight(stageSize.getHeight() / 2);
                        leaderboard.setPrefWidth(stageSize.getWidth() / 2);

                        hudPane.getChildren().add(leaderboard);
                        leaderboard.setLayoutX(stageSize.getHeight() * 0.4);
                        leaderboard.setLayoutY(stageSize.getHeight() / 4);
                        leaderboard.setBackground(Background.EMPTY);
                        leaderboardController.setHudPane(hudPane);
                        leaderboardController.setNode(leaderboard);
                        leaderboardController.setStageSize(stageSize);
                        leaderboardController.setStage(primaryStage, gameState);
                        leaderboardController.setScoreBoard(gameState.getScoreBoard());
                        leaderboardController.setLeaderBoard(gameState.getScoreBoard().getLeaderBoard());
                        leaderboardController.setAudioManager(audioManager);
                        leaderboardController.update();

                        primaryStage.setTitle("Leaderboard");
                        tab = true;
                        input.clear();
                    } else {
                        System.out.println("Tab hide info");
                        hudPane.getChildren().remove(leaderboard);
                        tab = false;

                    }
                }
                String code = e.getCode().toString();
                // only add each input command once
                if (!input.contains(code))
                    input.add(code);
            }
        });
    }

    public void initialiseGame(){

        // load hud
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../graphics/userInterface/fxmls/hud.fxml"));
        FXMLLoader loader_lb = new FXMLLoader(getClass().getResource("../graphics/userInterface/fxmls/leaderboard.fxml"));
        //Pane hudPane = new Pane();

        try {
            hudPane = loader.load();
            leaderboard = loader_lb.load();
            //get player attribute

        } catch (Exception e) {
            // todo make this better
            System.out.println("Crush in loading hud in map");
            e.printStackTrace();
            Platform.exit();
            System.exit(0);
        }

        leaderboardController = loader_lb.getController();
        HUDController hudController = loader.getController();
        hudController.setGameState(gameState);
        hudController.setMode(gameState.getMode());
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
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                hudPane.getChildren().add(canvas);
                int index = hudPane.getChildren().indexOf(canvas);
                hudPane.getChildren().get(index).toBack();


            }
        });

        // forces the game to be rendered behind the gui

        gc = canvas.getGraphicsContext2D();
        debugger = new Debugger(gc);

        renderer = new Renderer(gc, stageSize, debugger);


        //Creates the physics engine
        physicsEngine = new PhysicsController(gameState);
        audioManager.setGameMusic();
        audioManager.setNeonVolume(0);
        // initialise input controls
        beginClientLoop(renderer, hudController);
    }


}
