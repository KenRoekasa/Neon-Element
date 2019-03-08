package graphics.rendering;

import engine.entities.Character;
import engine.entities.Player;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Rectangle;

import static graphics.rendering.Renderer.getRelativeLocation;

/*
Calculates the relative position of the enemy with regards to the player, then calls the related DrawPlayer method
 */
class DrawEnemies {

    static void drawEnemy(GraphicsContext gc, Rectangle stage, Character enemy, Player player) {
        Point2D relativeLocation = getRelativeLocation(stage, enemy.getLocation(), player.getLocation());
        // subtract the width of the player to allow it to be centered
        relativeLocation = relativeLocation.add(-player.getWidth()/2f, -player.getWidth()/2f);
        DrawPlayers.drawPlayer(gc, relativeLocation, enemy);

    }

    static void drawerEnemyCursor(GraphicsContext gc, Rectangle stage, Character enemy, Player player) {
        Point2D relativeLocation = getRelativeLocation(stage, enemy.getLocation(), player.getLocation());
        DrawPlayers.drawCursor(gc, relativeLocation, enemy);
    }


    static void drawLightAttack(GraphicsContext gc, Character enemy ,Player player, long remainingAnimDuration, long animationDuration, Rectangle stage) {
        Point2D relativeLocation = getRelativeLocation(stage, enemy.getLocation(), player.getLocation());
        DrawStates.drawLightAttack(gc, enemy, remainingAnimDuration, animationDuration, relativeLocation);
    }


    static void drawHeavyAttackCharge(GraphicsContext gc, Character enemy, Player player, long remainingAnimDuration, long animationDuration, Rectangle stage) {
        Point2D relativeLocation = getRelativeLocation(stage, enemy.getLocation(), player.getLocation());
        DrawStates.drawHeavyAttackCharge(gc, enemy, remainingAnimDuration, animationDuration, relativeLocation);
    }


    static void drawHeavyAttack(GraphicsContext gc, Character enemy, Player player, long remainingAnimDuration, long animationDuration, Rectangle stage) {
        Point2D relativeLocation = getRelativeLocation(stage, enemy.getLocation(), player.getLocation());
        DrawStates.drawHeavyAttack(gc, player, remainingAnimDuration, animationDuration, relativeLocation);
    }


    static void drawShield(GraphicsContext gc, Character character, Player player, Rectangle stageSize) {
        Point2D relativeLocation = getRelativeLocation(stageSize, character.getLocation(), player.getLocation());
        DrawStates.drawShield(gc, character, relativeLocation);
    }
}
