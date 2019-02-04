package client;

import client.ClientGameState;
import client.InputHandler;
import debugger.Debugger;
import entities.CollisionDetection;
import entities.PhysicsObject;
import entities.Player;
import entities.PowerUp;
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
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Objects;


public class ClientBoard {

    private Debugger debugger;
    private GraphicsContext gc;
    private Stage primaryStage;


    private Rectangle stageSize;
    private ArrayList<String> input;

    private ClientGameState gameState;


    public ClientBoard(Stage primaryStage, ClientGameState gameState) throws Exception {
        // initial setup
        this.primaryStage = primaryStage;
        this.gameState = gameState;

        // load hud
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../userInterface/game_board.fxml"));
        Pane hudPane = new Pane();
        try {
            hudPane = (Pane) loader.load();
            //primaryStage.getScene().getRoot().getChildrenUnmodifiable().setAll((Node) loader.load());

        } catch (Exception e){
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


        Renderer renderer = new Renderer(gc, gameState, stageSize, debugger);


        beginClientLoop(renderer);


    }

    private void beginClientLoop(Renderer renderer) {
        new AnimationTimer() {
            public void handle(long currentNanoTime) {
                InputHandler.handleInput(gameState.getPlayer(), input, gameState.getMap());
                renderer.render(primaryStage);

            }
        }.start();
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

}
