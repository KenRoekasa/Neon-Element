package graphics.rendering.draw;

import engine.entities.Player;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Rectangle;

import static graphics.rendering.Renderer.xOffset;
import static graphics.rendering.Renderer.yOffset;


/*
    Calculates the relative location of the player and then calls the relative DrawPlayer class function
 */
public class DrawClientPlayer {

    public static void drawPlayer(GraphicsContext gc, Rectangle stage, Player player) {
        Point2D playerCenter = getStageCenter(stage);
        // subtract the width of the player to allow it to be centered
        playerCenter = playerCenter.add(-player.getWidth() / 2f, -player.getWidth() / 2f);
        DrawPlayers.drawPlayer(gc, playerCenter, player);

    }


    public static void drawPlayerCursor(GraphicsContext gc, Rectangle stageSize, Player player) {
        Point2D stageCenter = getStageCenter(stageSize);
        DrawPlayers.drawCursor(gc, stageCenter, player);
    }


    public static void drawLightAttack(GraphicsContext gc, Player player, long remainingAnimDuration, long animationDuration, Rectangle stage) {
        Point2D stageCenter = getStageCenter(stage);
        DrawStates.drawLightAttack(gc, player, remainingAnimDuration, animationDuration, stageCenter);
    }


    public static void drawHeavyAttackCharge(GraphicsContext gc, Player player, long remainingAnimDuration, long animationDuration, Rectangle stage) {
        Point2D stageCenter = getStageCenter(stage);
        DrawStates.drawHeavyAttackCharge(gc, player, remainingAnimDuration, animationDuration, stageCenter);
    }


    public static void drawHeavyAttack(GraphicsContext gc, Player player, long remainingAnimDuration, long animationDuration, Rectangle stage) {
        Point2D playerCenter = getStageCenter(stage);
        DrawStates.drawHeavyAttack(gc, player, remainingAnimDuration, animationDuration, playerCenter);
    }


    public static void drawShield(GraphicsContext gc, Player player, Rectangle stageSize) {
        Point2D playerCenter = getStageCenter(stageSize);
        DrawStates.drawShield(gc, player, playerCenter);
    }


    // returns the center of the stage - this is the location of the client player
    private static Point2D getStageCenter(Rectangle stageSize) {


        Point2D playerCenter = new Point2D(stageSize.getWidth() / 2, stageSize.getHeight() / 2);
        // add offset
        playerCenter = playerCenter.add(xOffset, yOffset);


        return playerCenter;
    }
}
