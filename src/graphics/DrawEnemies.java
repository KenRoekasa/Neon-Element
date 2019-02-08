package graphics;

import entities.Enemy;
import entities.Player;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Rectangle;

import static graphics.Renderer.getRelativeLocation;


class DrawEnemies {

    static void drawEnemy(GraphicsContext gc, Rectangle stage, Enemy enemy, Player player) {
        double yOffset = - enemy.getWidth() / 2f;

        Point2D relativeLocation = getRelativeLocation(stage, enemy, player);

        DrawPlayers.drawPlayer(gc, relativeLocation, enemy );

    }

    static void drawerEnemyCursor(GraphicsContext gc, Rectangle stage, Enemy enemy, Player player) {

        Point2D relativeLocation = getRelativeLocation(stage, enemy, player);

        DrawPlayers.drawCursor(gc, relativeLocation, enemy);
    }


    static void drawLightAttack(GraphicsContext gc, Enemy enemy ,Player player, long remainingAnimDuration, long animationDuration, Rectangle stage) {

        Point2D relativeLocation = getRelativeLocation(stage, enemy, player);

        DrawAttacks.drawLightAttack(gc, enemy, remainingAnimDuration, animationDuration, relativeLocation);
    }


    static void drawHeavyAttackCharge(GraphicsContext gc, Enemy enemy, Player player, long remainingAnimDuration, long animationDuration, Rectangle stage) {

        Point2D relativeLocation = getRelativeLocation(stage, enemy, player);

        DrawAttacks.drawHeavyAttackCharge(gc, enemy, remainingAnimDuration, animationDuration, relativeLocation);
    }


    static void drawHeavyAttack(GraphicsContext gc, Enemy enemy, Player player, long remainingAnimDuration, long animationDuration, Rectangle stage) {
        Point2D relativeLocation = getRelativeLocation(stage, enemy, player);

        DrawAttacks.drawHeavyAttack(gc, player, remainingAnimDuration, animationDuration, relativeLocation);
    }



}
