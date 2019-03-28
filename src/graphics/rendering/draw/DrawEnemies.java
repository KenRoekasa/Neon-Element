package graphics.rendering.draw;

import engine.entities.Character;
import engine.entities.Player;
import graphics.rendering.Renderer;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Rectangle;


/**
 * Contains methods that calculate the relative position of the enemy with regards to the player, then calls the relative drawing method
 */
public class DrawEnemies {

    /**
     * Calculates the enemies location on the screen
     *  Calls the drawPlayer() method from DrawPlayers
     * @param gc The GraphicsContext being drawn to
     * @param stageSize The size of the stage in which the GraphicsContext exists
     * @param enemy The enemy being drawn
     * @param player    The clients current player
     */
    public static void drawEnemy(GraphicsContext gc, Rectangle stageSize, Character enemy, Player player) {

        if(enemy.isAlive()) {
            Point2D relativeLocation = Renderer.getLocationRelativeToPlayer(stageSize, enemy.getLocation(), player.getLocation());
            // subtract the width of the player to allow it to be centered
            relativeLocation = relativeLocation.add(-player.getWidth()/2f, -player.getWidth()/2f);
            DrawPlayers.drawPlayer(gc, relativeLocation, enemy);
        }

    }

    /**
     * Calculates the enemies location on the screen
     * Calls the drawCursor() method from DrawPlayers
     * @param gc The GraphicsContext being drawn to
     * @param stageSize The size of the stage in which the GraphicsContext exists
     * @param enemy The enemy being drawn
     * @param player    The clients current player
     */
    public static void drawerEnemyCursor(GraphicsContext gc, Rectangle stageSize, Character enemy, Player player) {
        Point2D relativeLocation = Renderer.getLocationRelativeToPlayer(stageSize, enemy.getLocation(), player.getLocation());
        DrawPlayers.drawCursor(gc, relativeLocation, enemy);
    }


    /**
     * Calculates the enemies location on the screen
     * Calls the drawLightAttack() method from DrawPlayers
     * @param remainingAnimDuration The amount of time passed since the attack happened
     *                              Calculated by: character.getCurrentActionStart() + animationDuration - System.currentTimeMillis()
     * @param animationDuration The standard duration of a light attack
     * @param gc The GraphicsContext being drawn to
     * @param stageSize The size of the stage in which the GraphicsContext exists
     * @param enemy The enemy being drawn
     * @param player    The clients current player
     */
    public static void drawLightAttack(GraphicsContext gc, Character enemy, Player player, long remainingAnimDuration, long animationDuration, Rectangle stageSize) {
        Point2D relativeLocation = Renderer.getLocationRelativeToPlayer(stageSize, enemy.getLocation(), player.getLocation());
        DrawStates.drawLightAttack(gc, enemy, remainingAnimDuration, animationDuration, relativeLocation);
    }

    /**
     * Calculates the enemies location on the screen
     * Calls the drawHeavyAttackCharge() method from DrawPlayers
     * @param remainingAnimDuration The amount of time passed since the attack happened
     *                              Calculated by: character.getCurrentActionStart() + animationDuration - System.currentTimeMillis()
     * @param animationDuration The standard duration of a light attack
     * @param gc The GraphicsContext being drawn to
     * @param stageSize The size of the stage in which the GraphicsContext exists
     * @param enemy The enemy being drawn
     * @param player    The clients current player
     */
    public static void drawHeavyAttackCharge(GraphicsContext gc, Character enemy, Player player, long remainingAnimDuration, long animationDuration, Rectangle stageSize) {
        Point2D relativeLocation = Renderer.getLocationRelativeToPlayer(stageSize, enemy.getLocation(), player.getLocation());
        DrawStates.drawHeavyAttackCharge(gc, enemy, remainingAnimDuration, animationDuration, relativeLocation);
    }

    /**
     * Calculates the enemies location on the screen
     * Calls the drawHeavyAttack() method from DrawPlayers
     * @param remainingAnimDuration The amount of time passed since the attack happened
     *                              Calculated by: character.getCurrentActionStart() + animationDuration - System.currentTimeMillis()
     * @param animationDuration The standard duration of a light attack
     * @param gc The GraphicsContext being drawn to
     * @param stageSize The size of the stage in which the GraphicsContext exists
     * @param enemy The enemy being drawn
     * @param player    The clients current player
     */
    public static void drawHeavyAttack(GraphicsContext gc, Character enemy, Player player, long remainingAnimDuration, long animationDuration, Rectangle stageSize) {
        Point2D relativeLocation = Renderer.getLocationRelativeToPlayer(stageSize, enemy.getLocation(), player.getLocation());
        DrawStates.drawHeavyAttack(gc, player, remainingAnimDuration, animationDuration, relativeLocation);
    }

    /**
     * Calculates the enemies location on the screen
     * Calls the drawShield() method from DrawPlayers
     * @param gc The GraphicsContext being drawn to
     * @param stageSize The size of the stage in which the GraphicsContext exists
     * @param enemy The enemy being drawn
     * @param player    The clients current player
     */
    public static void drawShield(GraphicsContext gc, Character enemy, Player player, Rectangle stageSize) {
        Point2D relativeLocation = Renderer.getLocationRelativeToPlayer(stageSize, enemy.getLocation(), player.getLocation());
        DrawStates.drawShield(gc, enemy, relativeLocation);
    }
}
