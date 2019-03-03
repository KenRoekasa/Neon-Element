package graphics.rendering;

import engine.entities.Player;
import graphics.rendering.textures.Sprites;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;

import java.util.HashMap;

import static graphics.rendering.Renderer.xOffset;
import static graphics.rendering.Renderer.yOffset;


/*
    Calculates the relative location of the player and then calls the relative DrawPlayer class function
 */
class DrawClientPlayer {

    static void drawPlayer(GraphicsContext gc, Rectangle stage, Player player, HashMap<Sprites, Image> textures) {
        Point2D playerCenter = getPlayerCenter(stage);
        // subtract the width of the player to allow it to be centered
        playerCenter = playerCenter.add(-player.getWidth() / 2f, -player.getWidth() / 2f);

        // subtract the player offset


        DrawPlayers.drawPlayer(gc, playerCenter, player, textures);

    }


    static void drawPlayerCursor(GraphicsContext gc, Rectangle stageSize, Player player) {
        Point2D playerCenter = getPlayerCenter(stageSize);

        DrawPlayers.drawCursor(gc, playerCenter, player);
    }


    static void drawLightAttack(GraphicsContext gc, Player player, long remainingAnimDuration, long animationDuration, Rectangle stage) {
        Point2D playerCenter = getPlayerCenter(stage);
        DrawStates.drawLightAttack(gc, player, remainingAnimDuration, animationDuration, playerCenter);
    }


    static void drawHeavyAttackCharge(GraphicsContext gc, Player player, long remainingAnimDuration, long animationDuration, Rectangle stage) {
        Point2D playerCenter = getPlayerCenter(stage);
        DrawStates.drawHeavyAttackCharge(gc, player, remainingAnimDuration, animationDuration, playerCenter);
    }


    static void drawHeavyAttack(GraphicsContext gc, Player player, long remainingAnimDuration, long animationDuration, Rectangle stage) {
        Point2D playerCenter = getPlayerCenter(stage);
        DrawStates.drawHeavyAttack(gc, player, remainingAnimDuration, animationDuration, playerCenter);
    }


    static void drawShield(GraphicsContext gc, Player player, Rectangle stageSize) {
        Point2D playerCenter = getPlayerCenter(stageSize);
        DrawStates.drawShield(gc, player, playerCenter);
    }


    // returns the center of the stage - this is the location of the client player
    private static Point2D getPlayerCenter(Rectangle stageSize) {
        Point2D playerCenter = new Point2D(stageSize.getWidth() / 2, stageSize.getHeight() / 2);
        // add offset
        playerCenter = playerCenter.add(xOffset, yOffset);


        return playerCenter;
    }
}
