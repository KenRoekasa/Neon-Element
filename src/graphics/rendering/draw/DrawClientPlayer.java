package graphics.rendering.draw;

import engine.entities.Player;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Rectangle;

import static graphics.rendering.Renderer.xOffset;
import static graphics.rendering.Renderer.yOffset;


/**
 * Contains methods that calculate the relative location of the current clients player on the screen and then calls the relative drawing method
 */
public class DrawClientPlayer {

    /**
     *  Calculates the client players location on the screen
     *  Calls the drawPlayer() method from DrawPlayers
     * @param gc The GraphicsContext being drawn to
     * @param stageSize The size of the stage in which the GraphicsContext exists
     * @param player    The clients current player
     */
    public static void drawPlayer(GraphicsContext gc, Rectangle stageSize, Player player) {
        Point2D playerCenter = getStageCenter(stageSize);
        // subtract the width of the player to allow it to be centered
        playerCenter = playerCenter.add(-player.getWidth() / 2f, -player.getWidth() / 2f);
        DrawPlayers.drawPlayer(gc, playerCenter, player);

    }


    /**
     *  Calculates the client players location on the screen
     *  Calls the drawCursor() method from DrawPlayers
     * @param gc The GraphicsContext being drawn to
     * @param stageSize The size of the stage in which the GraphicsContext exists
     * @param player    The clients current player
     */
    public static void drawPlayerCursor(GraphicsContext gc, Rectangle stageSize, Player player) {
        Point2D stageCenter = getStageCenter(stageSize);
        DrawPlayers.drawCursor(gc, stageCenter, player);
    }


    /**
     * Calculates the client players location on the screen
     * Calls the drawLightAttack() method from DrawStates
     * @param gc The GraphicsContext being drawn to
     * @param player    The clients current player
     * @param remainingAnimDuration The amount of time passed since the attack happened
     *                              Calculated by: character.getCurrentActionStart() + animationDuration - System.currentTimeMillis()
     * @param animationDuration The standard duration of a light attack
     * @param stageSize The size of the stage in which the GraphicsContext exists
     */
    public static void drawLightAttack(GraphicsContext gc, Player player, long remainingAnimDuration, long animationDuration, Rectangle stageSize) {
        Point2D stageCenter = getStageCenter(stageSize);
        DrawStates.drawLightAttack(gc, player, remainingAnimDuration, animationDuration, stageCenter);
    }

    /**
     * Calculates the client players location on the screen
     * Calls the drawHeavyAttackCharge() method from DrawStates
     * @param gc The GraphicsContext being drawn to
     * @param player    The clients current player
     * @param remainingAnimDuration The amount of time passed since the attack happened
     *                              Calculated by: character.getCurrentActionStart() + animationDuration - System.currentTimeMillis()
     * @param animationDuration The standard duration of a light attack
     * @param stageSize The size of the stage in which the GraphicsContext exists
     */
    public static void drawHeavyAttackCharge(GraphicsContext gc, Player player, long remainingAnimDuration, long animationDuration, Rectangle stageSize) {
        Point2D stageCenter = getStageCenter(stageSize);
        DrawStates.drawHeavyAttackCharge(gc, player, remainingAnimDuration, animationDuration, stageCenter);
    }

    /**
     * Calculates the client players location on the screen
     * Calls the drawHeavyAttack() method from DrawStates
     * @param gc The GraphicsContext being drawn to
     * @param player    The clients current player
     * @param remainingAnimDuration The amount of time passed since the attack happened
     *                              Calculated by: character.getCurrentActionStart() + animationDuration - System.currentTimeMillis()
     * @param animationDuration The standard duration of a light attack
     * @param stageSize The size of the stage in which the GraphicsContext exists
     */
    public static void drawHeavyAttack(GraphicsContext gc, Player player, long remainingAnimDuration, long animationDuration, Rectangle stageSize) {
        Point2D playerCenter = getStageCenter(stageSize);
        DrawStates.drawHeavyAttack(gc, player, remainingAnimDuration, animationDuration, playerCenter);
    }

    /**
     * Calculates the client players location on the screen
     * Calls the drawShield() method from DrawStates
     * @param gc The GraphicsContext being drawn to
     * @param player    The clients current player
     * @param stageSize The size of the stage in which the GraphicsContext exists
     */
    public static void drawShield(GraphicsContext gc, Player player, Rectangle stageSize) {
        Point2D playerCenter = getStageCenter(stageSize);
        DrawStates.drawShield(gc, player, playerCenter);
    }

    /**
     *  Gets the location of the client player on the stage
     *  This is the center of the stage plus the current offset
     * @param stageSize The size of the stage
     * @return  The location of the player on the stage
     */
    private static Point2D getStageCenter(Rectangle stageSize) {
        Point2D playerCenter = new Point2D(stageSize.getWidth() / 2, stageSize.getHeight() / 2);
        // add offset
        playerCenter = playerCenter.add(xOffset, yOffset);

        return playerCenter;
    }
}
