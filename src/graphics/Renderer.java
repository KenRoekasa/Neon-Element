package graphics;

import calculations.AttackTimes;
import client.GameState;
import debugger.Debugger;
import entities.Character;
import entities.Enemy;
import entities.PhysicsObject;
import entities.Player;
import entities.PowerUp;
import enums.Action;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;


public class Renderer {
    private GraphicsContext gc;

    private Debugger debugger;

    private Rectangle stageSize;

    private static Point2D rotationCenter;


    public Renderer(GraphicsContext gc, Rectangle stageSize, Debugger debugger) {
        this.gc = gc;
        this.debugger = debugger;
        this.stageSize = stageSize;


    }

    public Renderer(GraphicsContext gc, Rectangle stageSize) {
        this.gc = gc;
        this.stageSize = stageSize;
        
    }

    public void render(Stage primaryStage, GameState gameState) {
        // clear screen
        gc.clearRect(0, 0, primaryStage.getWidth(), primaryStage.getHeight());

        rotationCenter = new Point2D(primaryStage.getWidth()/2, primaryStage.getHeight()/2);
        ISOConverter.applyRotationTransform(gc, rotationCenter);

        // draw map to screen
        DrawObjects.drawMap(gc, stageSize, gameState.getMap(), gameState.getPlayer());

        //sort based on proximity to the view (greater y is later)
        ArrayList<PhysicsObject> objects = sortDistance(gameState.getEntities());

        // draw all objects
        for (PhysicsObject o : objects) {
            renderObject(o, gameState);
        }

        // draw cursors to ensure on top
        for (Enemy e : gameState.getEnemies()) {
            DrawEnemies.drawerEnemyCursor(gc, stageSize, e, gameState.getPlayer());
        }

        DrawClientPlayer.drawPlayerCursor(gc, stageSize, gameState.getPlayer());

        gc.restore();
    }


    private void renderObject(PhysicsObject o, GameState gameState) {

        if (Objects.equals(o.getClass(), Player.class)) {
            Action status = gameState.getPlayer().getCurrentAction();
            Class charClass = Player.class;

            ActionSwitch(status, gameState.getPlayer(), charClass, gameState);
            DrawClientPlayer.drawPlayer(gc, stageSize, gameState.getPlayer());

        } else if (Objects.equals(o.getClass(), Enemy.class)) {
            Enemy enemy = (Enemy)o;
            Action status = enemy.getCurrentAction();
            Class charClass = Enemy.class;

            ActionSwitch(status, enemy, charClass, gameState);
            DrawEnemies.drawEnemy(gc, stageSize, (Enemy) o, gameState.getPlayer());

        } else if (Objects.equals(o.getClass(), PowerUp.class)) {
            DrawObjects.drawPowerUp(gc, stageSize, (PowerUp) o, gameState.getPlayer());
        }

    }

    private void ActionSwitch(Action status, Character character, Class charClass, GameState gameState){

        long animationDuration;
        long remainingAnimDuration;

        switch(status){
            case LIGHT:
                animationDuration = AttackTimes.getActionTime(Action.LIGHT);
                remainingAnimDuration = character.getCurrentActionStart() + animationDuration - System.currentTimeMillis();

                if(Objects.equals(charClass, Player.class)) {
                    DrawClientPlayer.drawLightAttack(gc, (Player)character, remainingAnimDuration, animationDuration, stageSize);
                } else {
                    DrawEnemies.drawLightAttack(gc, (Enemy)character, gameState.getPlayer(), remainingAnimDuration, animationDuration, stageSize);
                }
                break;
            case CHARGE:
                animationDuration = AttackTimes.getActionTime(Action.CHARGE);
                remainingAnimDuration = character.getCurrentActionStart() + animationDuration - System.currentTimeMillis();

                if(Objects.equals(charClass, Player.class)) {

                    DrawClientPlayer.drawHeavyAttackCharge(gc, gameState.getPlayer(), remainingAnimDuration, animationDuration, stageSize);
                } else {
                    DrawEnemies.drawHeavyAttackCharge(gc, (Enemy) character, gameState.getPlayer(), remainingAnimDuration, animationDuration, stageSize);
                }
                break;
            case HEAVY:
                animationDuration = AttackTimes.getActionTime(Action.HEAVY);
                remainingAnimDuration = character.getCurrentActionStart() + animationDuration - System.currentTimeMillis();

                if(Objects.equals(charClass, Player.class)) {
                    DrawClientPlayer.drawHeavyAttack(gc, gameState.getPlayer(), remainingAnimDuration, animationDuration, stageSize);
                } else {
                    DrawEnemies.drawHeavyAttack(gc, (Enemy) character, gameState.getPlayer(), remainingAnimDuration, animationDuration, stageSize);
                }

                break;
            case BLOCK:
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

    static Point2D getRelativeLocation(Rectangle stage, PhysicsObject obj, Player player) {
        Point2D enemyLocation = obj.getLocation();
        Point2D playerLocation = player.getLocation();

        double relativeX = stage.getWidth() / 2f - playerLocation.getX() + enemyLocation.getX() - obj.getWidth()/2f;
        double relativeY = stage.getHeight() / 2f - playerLocation.getY() + enemyLocation.getY() - obj.getWidth()/2f;
        return new Point2D(relativeX, relativeY);
    }

    static Point2D getRotationCenter() {
        return rotationCenter;
    }

}
