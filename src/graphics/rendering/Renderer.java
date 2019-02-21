package graphics.rendering;

import engine.calculations.AttackTimes;
import client.ClientGameState;
import graphics.debugger.Debugger;
import engine.entities.Character;
import engine.entities.PhysicsObject;
import engine.entities.Player;
import engine.entities.PowerUp;
import engine.enums.Action;
import engine.enums.ObjectType;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
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
    static HashMap<String, Image> textures;


    public Renderer(GraphicsContext gc, Rectangle stageSize, Debugger debugger) {
        this.gc = gc;
        this.debugger = debugger;
        this.stageSize = stageSize;
        textures = TextureLoader.loadTextures();

        stars = DrawObjects.loadStars(stageSize);

    }

    public Renderer(GraphicsContext gc, Rectangle stageSize) {
        this.gc = gc;
        this.stageSize = stageSize;
        textures = TextureLoader.loadTextures();
        stars = DrawObjects.loadStars(stageSize);
    }

    public void render(Stage primaryStage, ClientGameState gameState) {

        DrawObjects.drawBackground(gc, stageSize, stars);



        // apply screenshake
        // unsure about this
        //applyScreenshake(gameState);
        gc.save();

        if (gameState.getPlayer().isAlive()) {
            rotationCenter = new Point2D(primaryStage.getWidth()/2, primaryStage.getHeight()/2);
            ISOConverter.applyRotationTransform(gc, rotationCenter);

            // draw map to screen
            DrawObjects.drawMap(gc, stageSize, gameState.getMap(), gameState.getPlayer());

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
        } else {
            gc.setStroke(Color.WHITE);
            gc.strokeText("you are dead!", stageSize.getWidth()/2, stageSize.getHeight()/2);
            gc.restore();
        }

        debugger.print();
    }

    // render physics objects (players/pickups)
    private void renderObject(PhysicsObject o, ClientGameState gameState) {
        if (o.getTag() == ObjectType.PLAYER) {
            DrawClientPlayer.drawPlayer(gc, stageSize, gameState.getPlayer());
        } else if (o.getTag() == ObjectType.ENEMY) {
            DrawEnemies.drawEnemy(gc, stageSize, (Character) o, gameState.getPlayer());
        } else if (Objects.equals(o.getClass(), PowerUp.class)) {
            DrawObjects.drawPowerUp(gc, stageSize, (PowerUp) o, gameState.getPlayer());
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
    public static Point2D getRelativeLocation(Rectangle stage, PhysicsObject obj, Point2D playerLocation) {
        Point2D enemyLocation = obj.getLocation();

        double relativeX = stage.getWidth() / 2f - playerLocation.getX() + enemyLocation.getX();
        double relativeY = stage.getHeight() / 2f - playerLocation.getY() + enemyLocation.getY();


        return new Point2D(relativeX, relativeY);
    }

}
