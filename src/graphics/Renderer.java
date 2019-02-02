package graphics;

import client.GameState;
import debugger.Debugger;
import entities.Enemy;
import entities.PhysicsObject;
import entities.Player;
import entities.PowerUp;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;


public class Renderer {
    private GraphicsContext gc;
    private Debugger debugger;
    private Double scaleConstant;

    private Rectangle stageSize;

    private ArrayList<Pair<PhysicsObject, Long>> currentLightAttacks;


    public Renderer(GraphicsContext gc, Rectangle stageSize, Debugger debugger) {
        this.gc = gc;
        this.debugger = debugger;
        this.stageSize = stageSize;

        currentLightAttacks = new ArrayList<>();

        // magic number 10/7 * 990/1000
        scaleConstant = (double) 99 / 70;
    }

    public Renderer(GraphicsContext gc, Rectangle stageSize) {
        this.gc = gc;
        this.stageSize = stageSize;

        currentLightAttacks = new ArrayList<>();

        scaleConstant = (double) 99 / 70;
    }

    public void render(Stage primaryStage, GameState gameState) {
        // clear screen
        gc.clearRect(0, 0, primaryStage.getWidth(), primaryStage.getHeight());

        // draw map to screen
        DrawObjects.drawMap(gc, stageSize, gameState.getMap(), gameState.getPlayer(), scaleConstant);

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

        ArrayList<Pair<PhysicsObject, Long>> finishedLightAttacks = new ArrayList<>();

        // handle attack animations
        for (Pair<PhysicsObject, Long> attack : currentLightAttacks) {
            int animationDuration = 100;
            long remainingAnimDuration = attack.getValue() + animationDuration - System.currentTimeMillis();
            if (remainingAnimDuration >= 0) {
                debugger.add("Still attack animate", 1);

                if (Objects.equals(attack.getKey().getClass(), Player.class)) {
                    DrawPlayers.drawLightAttack(gc, gameState.getPlayer(), remainingAnimDuration, animationDuration, stageSize);
                } else if (Objects.equals(attack.getKey().getClass(), Enemy.class)) {


                }


            } else {
                finishedLightAttacks.add(attack);
            }
        }
        for (Pair<PhysicsObject, Long> attack : finishedLightAttacks) {
            currentLightAttacks.remove(attack);
        }


        DrawPlayers.drawerCursor(gc, stageSize, gameState.getPlayer());

        debugger.print();
    }

    private void renderObject(PhysicsObject o, GameState gameState) {

        if (Objects.equals(o.getClass(), Player.class)) {
            DrawPlayers.drawPlayer(gc, stageSize, gameState.getPlayer(), scaleConstant);
        } else if (Objects.equals(o.getClass(), Enemy.class)) {
            DrawEnemies.drawEnemy(gc, stageSize, (Enemy) o, gameState.getPlayer(), scaleConstant);

        } else if (Objects.equals(o.getClass(), PowerUp.class)) {
            DrawObjects.drawPowerUp(gc, stageSize, (PowerUp) o, gameState.getPlayer(), scaleConstant);
        }

    }

    private ArrayList<PhysicsObject> sortDistance(ArrayList<PhysicsObject> a) {
        a.sort(Comparator.comparingDouble(o -> o.getLocation().getY()));

        return a;
    }

    public void addPlayerAttack(Player player, int type) {

        Pair<PhysicsObject, Long> attack = new Pair<>(player, System.currentTimeMillis());

        if (type == 1) {
            currentLightAttacks.add(attack);
            debugger.add("Light attack!", 1);

        }
    }

    // this function takes a value and the range that value could be in, and maps it to its relevant position between two other values
    // see this https://www.arduino.cc/reference/en/language/functions/math/map/
    static long mapInRange(long x, long fromLow, long fromHigh, long toLow, long toHigh) {
        return (x - fromLow) * (toHigh - toLow) / (fromHigh - fromLow) + toLow;
    }

}
