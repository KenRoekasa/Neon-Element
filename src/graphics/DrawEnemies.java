package graphics;

import entities.Enemy;
import entities.Player;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Rectangle;

import static graphics.Renderer.getRelativeLocation;


class DrawEnemies {

    static void drawEnemy(GraphicsContext gc, Rectangle stage, Enemy enemy, Player player) {

        Point2D relativeLocation = getRelativeLocation(stage, enemy, player);
        relativeLocation = relativeLocation.add(-player.getWidth()/2f, -player.getWidth()/2f);

        DrawPlayers.drawPlayer(gc, relativeLocation, enemy );

    }

    static void drawerEnemyCursor(GraphicsContext gc, Rectangle stage, Enemy enemy, Player player) {

        Point2D relativeLocation = getRelativeLocation(stage, enemy, player);



        DrawPlayers.drawCursor(gc, relativeLocation, enemy);
    }


    static void drawLightAttack(GraphicsContext gc, Enemy enemy ,Player player, long remainingAnimDuration, long animationDuration, Rectangle stage) {

        Point2D relativeLocation = getRelativeLocation(stage, enemy, player);

        DrawStates.drawLightAttack(gc, enemy, remainingAnimDuration, animationDuration, relativeLocation);
    }


    static void drawHeavyAttackCharge(GraphicsContext gc, Enemy enemy, Player player, long remainingAnimDuration, long animationDuration, Rectangle stage) {

        Point2D relativeLocation = getRelativeLocation(stage, enemy, player);

        DrawStates.drawHeavyAttackCharge(gc, enemy, remainingAnimDuration, animationDuration, relativeLocation);
    }


    static void drawHeavyAttack(GraphicsContext gc, Enemy enemy, Player player, long remainingAnimDuration, long animationDuration, Rectangle stage) {
        Point2D relativeLocation = getRelativeLocation(stage, enemy, player);

        DrawStates.drawHeavyAttack(gc, player, remainingAnimDuration, animationDuration, relativeLocation);
    }


    public static void drawShield(GraphicsContext gc, Enemy character, Player player, Rectangle stageSize) {
        Point2D relativeLocation = getRelativeLocation(stageSize, character, player);
        DrawStates.drawShield(gc, character, relativeLocation);

    }
}
