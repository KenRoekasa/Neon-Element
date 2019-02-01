package graphics;

import client.GameState;
import debugger.Debugger;
import entities.Player;
import entities.PowerUp;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static javafx.scene.transform.Rotate.X_AXIS;


public class Renderer  {
    private GraphicsContext gc;
    private Debugger debugger;
    private Double scaleConstant;

    private GameState gameState;
    private Rectangle stageSize;


    public Renderer(GraphicsContext gc, GameState gameState, Rectangle stageSize, Debugger debugger) {
        this.gc = gc;
        this.debugger = debugger;
        this.gameState = gameState;
        this.stageSize = stageSize;


        // magic number 10/7 * 990/1000
        scaleConstant = (double)99/70;
    }

    public Renderer(GraphicsContext gc, GameState gameState, Rectangle stageSize){
        this.gc = gc;
        this.gameState = gameState;
        this.stageSize = stageSize;

        scaleConstant = (double)99/70;
    }

    public void render(Stage primaryStage) {
        // clear screen
        gc.clearRect(0, 0, primaryStage.getWidth(), primaryStage.getHeight());

        // draw to screen
        drawMap(stageSize, gameState.getMap(), gameState.getPlayer());
        drawerCursor(stageSize, gameState.getPlayer());
        //renderer.drawCrosshair(stageSize);
        drawPlayer(stageSize, gameState.getPlayer());
        drawEnemies(stageSize, gameState.getEnemies(), gameState.getPlayer());

        debugger.add((gameState.getPlayer().getLocation().toString()),1);
        debugger.print();
    }



    public void drawMap(Rectangle stage, Rectangle map, Player player) {

        Point2D stageCenter = new Point2D(stage.getWidth() / 2, stage.getHeight() / 2);
        Point2D isoPlayerLocation = ISOConverter.twoDToIso(player.getLocation());
        isoPlayerLocation = isoPlayerLocation.add(10, 0);

        double relativeX = stageCenter.getX() - isoPlayerLocation.getX() + player.getWidth()/2f;
        double relativeY = stageCenter.getY() - isoPlayerLocation.getY();
        Point2D boardPosition = new Point2D(relativeX, relativeY);

        applyIsoTransform(boardPosition.getX(), boardPosition.getY());

        // draw map
        gc.strokeRect(boardPosition.getX(), boardPosition.getY(), map.getWidth() * scaleConstant, map.getHeight() * scaleConstant);

        // restore previous state
        gc.restore();

    }

    @SuppressWarnings("Duplicates")
    void drawPowerUps(Rectangle stage, ArrayList<PowerUp> powerUps, Player player){
        for(PowerUp powerUp: powerUps){

            Point2D powerUpLocation = ISOConverter.twoDToIso(powerUp.getLocation());
            Point2D isoPlayerLocation = ISOConverter.twoDToIso(player.getLocation());

            double relativeX = stage.getWidth()/2f - isoPlayerLocation.getX() + powerUpLocation.getX();
            double relativeY = stage.getHeight()/2f - isoPlayerLocation.getY() + powerUpLocation.getY();

            Point2D relativeLocation = new Point2D(relativeX, relativeY);

            applyIsoTransform(relativeLocation.getX(), relativeLocation.getY());

            gc.fillOval(relativeLocation.getX(), relativeLocation.getY(), powerUp.getWidth() * scaleConstant, powerUp.getWidth() * scaleConstant);

            gc.restore();
        }
    }

    @SuppressWarnings("Duplicates")
    void drawEnemies(Rectangle stage, ArrayList<Player> enemies, Player player){
        for(Player enemy: enemies) {

            // calculates graphical location of object based on its location on board and players location on board
            Point2D enemyLocation = ISOConverter.twoDToIso(enemy.getLocation());
            Point2D isoPlayerLocation = ISOConverter.twoDToIso(player.getLocation());

            double relativeX = stage.getWidth()/2f - isoPlayerLocation.getX() + enemyLocation.getX();
            double relativeY = stage.getHeight()/2f - isoPlayerLocation.getY() + enemyLocation.getY() - enemy.getWidth()/2f;
            Point2D relativeLocation = new Point2D(relativeX, relativeY);

            applyIsoTransform(relativeLocation.getX(), relativeLocation.getY());

            //draw enemy
            gc.fillRect(relativeLocation.getX(), relativeLocation.getY(), player.getWidth()*scaleConstant, player.getWidth()*scaleConstant);

            // restore prev state
            gc.restore();

        }
    }

    public void drawPlayer(Rectangle stage, Player player) {

        // Point2D isoLocation = ISOConverter.twoDToIso(playerLocation);
        Point2D stageCenter = new Point2D(stage.getWidth() / 2, stage.getHeight() / 2);

        double playerXCenter = stageCenter.getX();
        double playerYCenter = stageCenter.getY();

        applyIsoTransform(playerXCenter, playerYCenter);

        gc.setFill(Color.GREEN);

        double centerAdjust = player.getWidth()/2f * scaleConstant;

        gc.fillRect(playerXCenter - centerAdjust, playerYCenter - centerAdjust, player.getWidth() * scaleConstant, player.getWidth()* scaleConstant);

        // has to be called after applying transform
        gc.restore();

        debugger.add("Player location: " + player.getLocation().toString(), 1);
    }


    @SuppressWarnings("Duplicates")
    private void applyIsoTransform(double xRotationCenter, double yRotationCenter) {
        gc.save();
        Affine affine = new Affine();

        Rotate rotateX= new Rotate(45, xRotationCenter, yRotationCenter);
        Rotate rotateZ = new Rotate(60.0, xRotationCenter, yRotationCenter, 0, X_AXIS);
        affine.prepend(rotateX);
        affine.prepend(rotateZ);

        gc.transform(affine);
    }

    void drawCrosshair(Rectangle stage){
        gc.strokeLine(stage.getWidth()/2, 0, stage.getWidth()/2, stage.getHeight());
        gc.strokeLine(0, stage.getHeight()/2, stage.getWidth(), stage.getHeight()/2);
    }


    @SuppressWarnings("Duplicates")
    void drawerCursor(Rectangle stage, Player player) {

        int cursorRadius = 10;

        Point2D stageCenter = new Point2D(stage.getWidth() / 2, stage.getHeight() / 2);
        double playerXCenter = stageCenter.getX();
        double playerYCenter = stageCenter.getY();

        gc.save();
        Affine affine = new Affine();

        affine.prependRotation(player.getPlayerAngle().getAngle(), playerXCenter, playerYCenter);

        Rotate rotateX = new Rotate(45, playerXCenter, playerYCenter);
        Rotate rotateZ = new Rotate(60.0, playerXCenter, playerYCenter, 0, X_AXIS);
        affine.prepend(rotateX);
        affine.prepend(rotateZ);
        gc.transform(affine);
        gc.setFill(Color.RED);

        gc.fillOval(playerXCenter - cursorRadius/2f, playerYCenter - cursorRadius/2f - 30, cursorRadius, cursorRadius);

        gc.restore();
    }
}