package graphics.rendering;

import engine.model.AttackTimes;
import client.ClientGameState;
import engine.model.gametypes.HillGame;
import graphics.debugger.Debugger;
import engine.entities.Character;
import engine.entities.PhysicsObject;
import engine.entities.Player;
import engine.entities.PowerUp;
import engine.model.enums.Action;
import engine.model.enums.ObjectType;
import graphics.rendering.draw.DrawClientPlayer;
import graphics.rendering.draw.DrawEnemies;
import graphics.rendering.draw.DrawObjects;
import graphics.rendering.textures.Sprites;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import graphics.rendering.textures.TextureLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Objects;


/**
 * Handles the rendering of a GameState to the Stage
 */
public class Renderer {

    /**
     *  The GraphicsContext being drawn too
     */
    private GraphicsContext gc;

    /**
     * The debugger used
     */
    private Debugger debugger;

    /**
     * The size of the Stage being renderer to
     */
    private Rectangle stageSize;

    /**
     * An ArrayList of the locations of stars in the background of the game
     */
    private ArrayList<Point2D> stars;

    /**
     *  A HashMap of all the available textures in the game
     */
    public static HashMap<Sprites, Image> textures;

    /**
     *  A float storing the current xOffset of the map
     */
    public static float xOffset;

    /**
     * A float storing the current yOffset of the map
     */
    public static float yOffset;

    /**
     * The font used on the death screen
     */
    private Font deathFont;

    /**
     * Constructor for the renderer object
     * @param gc The GraphicsContext to draw to
     * @param stageSize The size of the Stage being drawn to
     * @param debugger  The debugger to use
     */
    public Renderer(GraphicsContext gc, Rectangle stageSize, Debugger debugger) {
        this.gc = gc;
        this.debugger = debugger;
        this.stageSize = stageSize;
        textures = TextureLoader.loadTextures();

        stars = DrawObjects.loadStars(stageSize);
        xOffset = 0;
        yOffset = 0;

        try {
            deathFont = Font.loadFont(new FileInputStream(new File("src/graphics/userInterface/resources/fonts/Audiowide.ttf")), 50);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    /**
     * Constructor for the renderer object
     * @param gc The GraphicsContext to draw to
     * @param stageSize The size of the Stage being drawn to
     */
    public Renderer(GraphicsContext gc, Rectangle stageSize) {
        this.gc = gc;
        this.stageSize = stageSize;
        textures = TextureLoader.loadTextures();
        stars = DrawObjects.loadStars(stageSize);
        xOffset = 0;
        yOffset = 0;

        try {
            deathFont = Font.loadFont(new FileInputStream(new File("src/graphics/userInterface/resources/fonts/Audiowide.ttf")), 50);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    /**
     * The method called every tick to render objects to the screen
     * @param primaryStage  The size of the Stage being drawn to
     * @param gameState The current GameState to be drawn
     */
    public void render(Stage primaryStage, ClientGameState gameState) {

        DrawObjects.drawBackground(gc, stageSize, stars);

        gc.save();

        if (gameState.getPlayer().isAlive()) {

            calculateOffset(gameState);

            Point2D rotationCenter = new Point2D(primaryStage.getWidth() / 2, primaryStage.getHeight() / 2);
            ISOConverter.applyRotationTransform(gc, rotationCenter);

            // draw map to screen
            DrawObjects.drawMap(gc, stageSize, gameState.getMap().getGround(), gameState.getPlayer(), textures);

            if(Objects.equals(gameState.getGameType() .getClass(), HillGame.class)) {
                DrawObjects.drawHill(gc, stageSize, gameState.getPlayer(), ((HillGame) gameState.getGameType()).getHill());
            }


            //sort based on proximity to the view (greater y is later)
            ArrayList<PhysicsObject> objects = sortDistance(gameState.getObjects());

            // draw all objects
            for (PhysicsObject o : objects) {
                renderObject(o, gameState);
            }

            // draw cursors to ensure on top
            for (Character e : gameState.getOtherPlayers(gameState.getPlayer())) {
                DrawEnemies.drawerEnemyCursor(gc, stageSize, e, gameState.getPlayer());
                if(!(e.getCurrentAction() == Action.IDLE)) {
                    ActionSwitch(e.getCurrentAction(), e, gameState);
                }
            }

            DrawClientPlayer.drawPlayerCursor(gc, stageSize, gameState.getPlayer());

            if(!(gameState.getPlayer().getCurrentAction() == Action.IDLE)) {
                ActionSwitch(gameState.getPlayer().getCurrentAction(), gameState.getPlayer(), gameState);
            }
            ActionSwitch(gameState.getPlayer().getCurrentAction(), gameState.getPlayer(), gameState);

            gc.restore();
            debugger.gameStateDebugger(gameState, stageSize);
            //debugger.simpleGSDebugger(gameState, debugger);
        } else {
            // reset render offset
            xOffset = 0;
            yOffset = 0;

            gc.setFont(deathFont);
            gc.setStroke(Color.WHITE);
            gc.strokeText("you are dead!", stageSize.getWidth()/2 - 170, stageSize.getHeight()/2);
            gc.restore();
        }


        debugger.print();
    }

    /**
     * Calculates the offset at which to render the game
     * Offset is based on the players distance to a wall
     * @param gameState The current GameState
     */
    private void calculateOffset(ClientGameState gameState) {
        // todo make this exponential
        int maxAllowedDistance = 300;

        // calculate xOffset
        if(gameState.getPlayer().getLocation().getX() <= maxAllowedDistance ) {
            xOffset = -(maxAllowedDistance - (float) gameState.getPlayer().getLocation().getX());

        } else if(gameState.getMap().getGround().getWidth() - maxAllowedDistance <= gameState.getPlayer().getLocation().getX()) {
            xOffset = -(float) (gameState.getMap().getGround().getWidth() - gameState.getPlayer().getLocation().getX() - maxAllowedDistance);
        }

        // calculate yOffset
        if(gameState.getPlayer().getLocation().getY() <= maxAllowedDistance ) {
            yOffset = -(maxAllowedDistance - (float) gameState.getPlayer().getLocation().getY());
        } else if(gameState.getMap().getGround().getHeight() - maxAllowedDistance <= gameState.getPlayer().getLocation().getY()) {
            yOffset = -(float) (gameState.getMap().getGround().getHeight() - gameState.getPlayer().getLocation().getY() - maxAllowedDistance);
        }


    }

    /**
     * Method called inside the Render method
     * Used to render any PhysicsObject
     * @param object The object being rendered
     * @param gameState The current GameState
     */
    private void renderObject(PhysicsObject object, ClientGameState gameState) {
        if (object.getTag() == ObjectType.PLAYER) {
            DrawClientPlayer.drawPlayer(gc, stageSize, gameState.getPlayer());
        } else if (object.getTag() == ObjectType.ENEMY) {
            DrawEnemies.drawEnemy(gc, stageSize, (Character) object, gameState.getPlayer());
        } else if (Objects.equals(object.getClass(), PowerUp.class)) {
            DrawObjects.drawPowerUp(gc, stageSize, (PowerUp) object, gameState.getPlayer());
        } else if (object.getTag() == ObjectType.OBSTACLE) {
            DrawObjects.drawWalls(gc, stageSize, object, gameState.getPlayer());
        }
    }

    // render the action of the provided player

    /**
     * Method called inside the Render method
     * Used to render the actions of a player
     * @param action The action taking place
     * @param player    The player doing said action
     * @param gameState The current GameState
     */
    private void ActionSwitch(Action action, Character player, ClientGameState gameState){
        long animationDuration;
        long remainingAnimDuration;

        switch(action){
            case LIGHT:
                animationDuration = AttackTimes.getActionTime(Action.LIGHT);
                remainingAnimDuration = player.getCurrentActionStart() + animationDuration - System.currentTimeMillis();

                if(player.getTag() == ObjectType.PLAYER) {
                    DrawClientPlayer.drawLightAttack(gc, (Player)player, remainingAnimDuration, animationDuration, stageSize);
                } else {
                    DrawEnemies.drawLightAttack(gc, player, gameState.getPlayer(), remainingAnimDuration, animationDuration, stageSize);
                }
                break;
            case CHARGE:
                animationDuration = AttackTimes.getActionTime(Action.CHARGE);
                remainingAnimDuration = player.getCurrentActionStart() + animationDuration - System.currentTimeMillis();

                if(player.getTag() == ObjectType.PLAYER) {

                    DrawClientPlayer.drawHeavyAttackCharge(gc, gameState.getPlayer(), remainingAnimDuration, animationDuration, stageSize);
                } else {
                    DrawEnemies.drawHeavyAttackCharge(gc, player, gameState.getPlayer(), remainingAnimDuration, animationDuration, stageSize);
                }
                break;
            case HEAVY:
                animationDuration = AttackTimes.getActionTime(Action.HEAVY);
                remainingAnimDuration = player.getCurrentActionStart() + animationDuration - System.currentTimeMillis();

                if(player.getTag() == ObjectType.PLAYER) {
                    DrawClientPlayer.drawHeavyAttack(gc, gameState.getPlayer(), remainingAnimDuration, animationDuration, stageSize);
                } else {
                    DrawEnemies.drawHeavyAttack(gc, player, gameState.getPlayer(), remainingAnimDuration, animationDuration, stageSize);
                }
                break;
            case BLOCK:
                if(player.getTag() == ObjectType.PLAYER) {
                    DrawClientPlayer.drawShield(gc, gameState.getPlayer(), stageSize);
                } else {
                    DrawEnemies.drawShield(gc, player, gameState.getPlayer(), stageSize);
                }
                break;
        }
    }

    /**
     * Used to sort the isometric distance of objects from the location of the player view
     * Objects higher up the screen are rendered first as they are 'behind' objects further down the screen
     * @param objects The list of objects to sort
     * @return  The sorted list of objects
     */
    private ArrayList<PhysicsObject> sortDistance(ArrayList<PhysicsObject> objects) {
        objects.sort(Comparator.comparingDouble(o -> o.getLocation().getY()));
        return objects;
    }



    /**
     * Function that takes a value and the range that value could be in, and maps it to its relevant position between two other values
     * taken from https://www.arduino.cc/reference/en/language/functions/math/map/
     * @param x The input value being mapped
     * @param fromLow   The lowest possible input value
     * @param fromHigh  The highest possible input value
     * @param toLow The lowest range of the possible output values
     * @param toHigh    The high range of the possible output values
     * @return  The mapped value
     */
    public static long mapInRange(long x, long fromLow, long fromHigh, long toLow, long toHigh) {
        return (x - fromLow) * (toHigh - toLow) / (fromHigh - fromLow) + toLow;
    }

    /**
     * Gets the screen location of an object relative to the player in cartesian space
     * This is prior to the isometric transformation
     * @param stageSize The size of the current Stage
     * @param objectLocation    The location of the object
     * @param playerLocation    The location of the client player
     * @return  The relative cartesian render location of the object
     */
    // get relative location of an object with regards to player - allows for player to be central
    public static Point2D getLocationRelativeToPlayer(Rectangle stageSize, Point2D objectLocation, Point2D playerLocation) {

        double relativeX = stageSize.getWidth() / 2f - playerLocation.getX() + objectLocation.getX();
        double relativeY = stageSize.getHeight() / 2f - playerLocation.getY() + objectLocation.getY();

        relativeX += xOffset;
        relativeY += yOffset;

        return new Point2D(relativeX, relativeY);
    }

    /**
     * Gets the location of the client player in cartesian space
     * This is prior to the isometric transformation
     * @param stageSize The size of the current Stage
     * @param playerLocation    The location of the client player
     * @return  The relative cartesian render location of the player
     */
    public static Point2D getPlayerRelativeLocation(Rectangle stageSize, Point2D playerLocation) {

        double relativeX = stageSize.getWidth() / 2f - playerLocation.getX();
        double relativeY = stageSize.getHeight() / 2f - playerLocation.getY();

        relativeX += xOffset;
        relativeY += yOffset;

        return new Point2D(relativeX, relativeY);
    }

}
