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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Objects;


public class Renderer {
    private GraphicsContext gc;

    private Debugger debugger;
    private Rectangle stageSize;
    private static Point2D rotationCenter;
    private ArrayList<Point2D> stars;
    static HashMap<Sprites, Image> textures;
    public static float xOffset;
    public static float yOffset;



    public Renderer(GraphicsContext gc, Rectangle stageSize, Debugger debugger) {
        this.gc = gc;
        this.debugger = debugger;
        this.stageSize = stageSize;
        textures = TextureLoader.loadTextures();

        stars = DrawObjects.loadStars(stageSize);
        xOffset = 0;
        yOffset = 0;

    }

    public Renderer(GraphicsContext gc, Rectangle stageSize) {
        this.gc = gc;
        this.stageSize = stageSize;
        textures = TextureLoader.loadTextures();
        stars = DrawObjects.loadStars(stageSize);
        xOffset = 0;
        yOffset = 0;

    }

    public void render(Stage primaryStage, ClientGameState gameState) {

        DrawObjects.drawBackground(gc, stageSize, stars);


        gc.save();

        if ( gameState.getPlayer().isAlive()) {

            calculateOffset(gameState);

            rotationCenter = new Point2D(primaryStage.getWidth()/2, primaryStage.getHeight()/2);
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

            gc.setFont(new Font("graphics/userInterface/resources/fonts/Super Mario Bros.ttf", 50));
            gc.setStroke(Color.WHITE);
            gc.strokeText("you are dead!", stageSize.getWidth()/2, stageSize.getHeight()/2);
            gc.restore();
        }


        debugger.print();
    }

    private void calculateOffset(ClientGameState gameState) {
        // todo make this exponential
        int maxAllowedDistance = 300;

        if(gameState.getPlayer().getLocation().getX() <= maxAllowedDistance ) {
            xOffset = -(maxAllowedDistance - (float) gameState.getPlayer().getLocation().getX());

        } else if(gameState.getMap().getGround().getWidth() - maxAllowedDistance <= gameState.getPlayer().getLocation().getX()) {
            xOffset = -(float) (gameState.getMap().getGround().getWidth() - gameState.getPlayer().getLocation().getX() - maxAllowedDistance);
        }

        if(gameState.getPlayer().getLocation().getY() <= maxAllowedDistance ) {
            yOffset = -(maxAllowedDistance - (float) gameState.getPlayer().getLocation().getY());
        } else if(gameState.getMap().getGround().getHeight() - maxAllowedDistance <= gameState.getPlayer().getLocation().getY()) {
            yOffset = -(float) (gameState.getMap().getGround().getHeight() - gameState.getPlayer().getLocation().getY() - maxAllowedDistance);
        }


    }

    // render physics objects (players/pickups)
    private void renderObject(PhysicsObject o, ClientGameState gameState) {
        if (o.getTag() == ObjectType.PLAYER) {
            DrawClientPlayer.drawPlayer(gc, stageSize, gameState.getPlayer());
        } else if (o.getTag() == ObjectType.ENEMY) {
            DrawEnemies.drawEnemy(gc, stageSize, (Character) o, gameState.getPlayer());
        } else if (Objects.equals(o.getClass(), PowerUp.class)) {
            DrawObjects.drawPowerUp(gc, stageSize, (PowerUp) o, gameState.getPlayer());
        } else if (o.getTag() == ObjectType.OBSTACLE) {
            DrawObjects.drawObstacles(gc, stageSize, (PhysicsObject) o, gameState.getPlayer());
        }
    }

    // render the action of the provided player
    private void ActionSwitch(Action status, Character character, ClientGameState gameState){
        long animationDuration;
        long remainingAnimDuration;

        switch(status){
            case LIGHT:
                animationDuration = AttackTimes.getActionTime(Action.LIGHT);
                remainingAnimDuration = character.getCurrentActionStart() + animationDuration - System.currentTimeMillis();

                if(character.getTag() == ObjectType.PLAYER) {
                    DrawClientPlayer.drawLightAttack(gc, (Player)character, remainingAnimDuration, animationDuration, stageSize);
                } else {
                    DrawEnemies.drawLightAttack(gc, character, gameState.getPlayer(), remainingAnimDuration, animationDuration, stageSize);
                }
                break;
            case CHARGE:
                animationDuration = AttackTimes.getActionTime(Action.CHARGE);
                remainingAnimDuration = character.getCurrentActionStart() + animationDuration - System.currentTimeMillis();

                if(character.getTag() == ObjectType.PLAYER) {

                    DrawClientPlayer.drawHeavyAttackCharge(gc, gameState.getPlayer(), remainingAnimDuration, animationDuration, stageSize);
                } else {
                    DrawEnemies.drawHeavyAttackCharge(gc, character, gameState.getPlayer(), remainingAnimDuration, animationDuration, stageSize);
                }
                break;
            case HEAVY:
                animationDuration = AttackTimes.getActionTime(Action.HEAVY);
                remainingAnimDuration = character.getCurrentActionStart() + animationDuration - System.currentTimeMillis();

                if(character.getTag() == ObjectType.PLAYER) {
                    DrawClientPlayer.drawHeavyAttack(gc, gameState.getPlayer(), remainingAnimDuration, animationDuration, stageSize);
                } else {
                    DrawEnemies.drawHeavyAttack(gc, character, gameState.getPlayer(), remainingAnimDuration, animationDuration, stageSize);
                }
                break;
            case BLOCK:
                if(character.getTag() == ObjectType.PLAYER) {
                    DrawClientPlayer.drawShield(gc, gameState.getPlayer(), stageSize);
                } else {
                    DrawEnemies.drawShield(gc, character, gameState.getPlayer(), stageSize);
                }
                break;
        }
    }

    private void applyScreenshake(ClientGameState gameState) {
        if(gameState.getPlayer().getCurrentAction() == Action.LIGHT) {
            float x = ((float) Math.random() * 10) - 5;
            float y = ((float) Math.random() * 10) - 5;

            Transform t = new Translate(x, y);

            Affine a = gc.getTransform();
            a.prepend(t);

            gc.setTransform(a);
        }
    }

    private ArrayList<PhysicsObject> sortDistance(ArrayList<PhysicsObject> a) {
        a.sort(Comparator.comparingDouble(o -> o.getLocation().getY()));
        return a;
    }

    // this function takes a value and the range that value could be in, and maps it to its relevant position between two other values

    // see this https://www.arduino.cc/reference/en/language/functions/math/map/
    static long mapInRange(long x, long fromLow, long fromHigh, long toLow, long toHigh) {
        return (x - fromLow) * (toHigh - toLow) / (fromHigh - fromLow) + toLow;
    }

    // get relative location of an object with regards to player - allows for player to be central
    public static Point2D getRelativeLocation(Rectangle stage, Point2D objectLocation, Point2D playerLocation) {

        double relativeX = stage.getWidth() / 2f - playerLocation.getX() + objectLocation.getX();
        double relativeY = stage.getHeight() / 2f - playerLocation.getY() + objectLocation.getY();

        relativeX += xOffset;
        relativeY += yOffset;


        return new Point2D(relativeX, relativeY);
    }

    public static Point2D getRelativeLocation(Rectangle stage, Point2D playerLocation) {


        double relativeX = stage.getWidth() / 2f - playerLocation.getX();
        double relativeY = stage.getHeight() / 2f - playerLocation.getY();

        relativeX += xOffset;
        relativeY += yOffset;


        return new Point2D(relativeX, relativeY);
    }

}
