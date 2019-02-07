package graphics;

import debugger.Debugger;
import entities.Player;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Rectangle;

public class DrawClientPlayer {
    static void drawPlayer(GraphicsContext gc, Rectangle stage, Player player) {
        Point2D playerCenter = getStageCenter(stage);

        double centerAdjust = player.getWidth()/2f;
        playerCenter = playerCenter.add(0, - centerAdjust);

        DrawPlayers.drawPlayer(gc, playerCenter, player);

    }

    static void drawPlayerCursor(GraphicsContext gc, Rectangle stageSize, Player player) {
        Point2D stageCenter = getStageCenter(stageSize);
        DrawPlayers.drawCursor(gc, stageCenter, player );
    }


    static void drawLightAttack(GraphicsContext gc, Player player, long remainingAnimDuration, long animationDuration, Rectangle stage) {

        Point2D stageCenter = getStageCenter(stage);
        DrawAttacks.drawLightAttack(gc, player, remainingAnimDuration, animationDuration, stageCenter);
    }


    static void drawHeavyAttackCharge(GraphicsContext gc, Player player, long remainingAnimDuration, long animationDuration, Rectangle stage) {

        Point2D stageCenter = getStageCenter(stage);

        DrawAttacks.drawHeavyAttackCharge(gc, player, remainingAnimDuration, animationDuration, stageCenter);
    }


    static void drawHeavyAttack(GraphicsContext gc, Player player, long remainingAnimDuration, long animationDuration, Rectangle stage) {
        Point2D playerCenter = getStageCenter(stage);

        DrawAttacks.drawHeavyAttack(gc, player, remainingAnimDuration, animationDuration, playerCenter);
    }



    private static Point2D getStageCenter(Rectangle stageSize) {
        return new Point2D(stageSize.getWidth() / 2, stageSize.getHeight() / 2);
    }

}
