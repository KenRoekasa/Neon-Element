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
import javafx.scene.shape.Rectangle;
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
    private HashMap<String, Image> textures;


    public Renderer(GraphicsContext gc, Rectangle stageSize, Debugger debugger) {
        this.gc = gc;
        this.debugger = debugger;
        this.stageSize = stageSize;
        this.textures = TextureLoader.loadTextures();

        stars = DrawObjects.loadStars(stageSize);

    }

    public Renderer(GraphicsContext gc, Rectangle stageSize) {
        this.gc = gc;
        this.stageSize = stageSize;
        this.textures = TextureLoader.loadTextures();


        stars = DrawObjects.loadStars(stageSize);
    }

    public void render(Stage primaryStage, ClientGameState gameState) {
        // clear screen
        gc.clearRect(0, 0, primaryStage.getWidth(), primaryStage.getHeight());

        DrawObjects.drawBackground(gc, stageSize, stars);

        rotationCenter = new Point2D(primaryStage.getWidth()/2, primaryStage.getHeight()/2);
        ISOConverter.applyRotationTransform(gc, rotationCenter);

        // draw map to screen
        DrawObjects.drawMap(gc, stageSize, gameState.getMap(), gameState.getPlayer(), textures.get("background"));

        //sort based on proximity to the view (greater y is later)
        ArrayList<PhysicsObject> objects = sortDistance(gameState.getEntities());

        // draw all objects
        for (PhysicsObject o : objects) {
            renderObject(o, gameState);
        }

        // draw cursors to ensure on top
        for (Character e : gameState.getEnemies()) {
            DrawEnemies.drawerEnemyCursor(gc, stageSize, e, gameState.getPlayer());
        }

        DrawClientPlayer.drawPlayerCursor(gc, stageSize, gameState.getPlayer());

        gc.restore();

        debugger.gameStateDebugger(gameState, stageSize);

        debugger.print();
    }


    private void renderObject(PhysicsObject o, ClientGameState gameState) {

        if (o.getTag() == ObjectType.PLAYER) {
            Action status = gameState.getPlayer().getCurrentAction();
            
            DrawClientPlayer.drawPlayer(gc, stageSize, gameState.getPlayer());
            ActionSwitch(status, gameState.getPlayer(), gameState);

        } else if (o.getTag() == ObjectType.ENEMY) {
            Character enemy = (Character) o;
            Action status = enemy.getCurrentAction();

            DrawEnemies.drawEnemy(gc, stageSize, enemy, gameState.getPlayer());
            ActionSwitch(status, enemy, gameState);

        } else if (Objects.equals(o.getClass(), PowerUp.class)) {
            DrawObjects.drawPowerUp(gc, stageSize, (PowerUp) o, gameState.getPlayer());
        }

    }

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

    public static Point2D getRelativeLocation(Rectangle stage, PhysicsObject obj, Point2D playerLocation ) {
        Point2D enemyLocation = obj.getLocation();

        double relativeX = stage.getWidth() / 2f - playerLocation.getX() + enemyLocation.getX();
        double relativeY = stage.getHeight() / 2f - playerLocation.getY() + enemyLocation.getY();


        return new Point2D(relativeX, relativeY);
    }

    static Point2D getRotationCenter() {
        return rotationCenter;
    }

}
