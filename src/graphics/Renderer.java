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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;


public class Renderer {
    private GraphicsContext gc;
    private Debugger debugger;
    private Double scaleConstant;

    private Rectangle stageSize;


    public Renderer(GraphicsContext gc, Rectangle stageSize, Debugger debugger) {
        this.gc = gc;
        this.debugger = debugger;
        this.stageSize = stageSize;


        // magic number 10/7 * 990/1000
        scaleConstant = (double) 99 / 70;
    }

    public Renderer(GraphicsContext gc, Rectangle stageSize) {
        this.gc = gc;
        this.stageSize = stageSize;

        scaleConstant = (double) 99 / 70;
    }

    public void render(Stage primaryStage, GameState gameState) {
        // clear screen
        gc.clearRect(0, 0, primaryStage.getWidth(), primaryStage.getHeight());

        // draw to screen
        DrawObjects.drawMap(gc, stageSize, gameState.getMap(), gameState.getPlayer(), scaleConstant);

        //sort based on proximity to the view (greater y is later)
        ArrayList<PhysicsObject> objects = sortDistance(gameState.getEntities());

        // render all objects
        for (PhysicsObject o : objects) {
            renderObject(o, gameState);
        }

        // render cursors to ensure on top
        for (Enemy e : gameState.getEnemies()) {
            DrawEnemies.drawerEnemyCursor(gc, stageSize, e, gameState.getPlayer());
        }
        DrawPlayers.drawerCursor(gc, stageSize, gameState.getPlayer());

        debugger.add((gameState.getPlayer().getLocation().toString()), 1);
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






}
