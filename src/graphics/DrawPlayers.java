package graphics;

import entities.Character;

import enumSwitches.colourSwitch;
import enums.Action;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class DrawPlayers {

    static void drawPlayer(GraphicsContext gc, Point2D playerCenter, Character player) {


        Color c = colourSwitch.getElementColour(player.getCurrentElement());
        gc.setFill(c);

        gc.fillRect(playerCenter.getX(), playerCenter.getY(), player.getWidth(), player.getWidth());

    }

    static void drawCursor(GraphicsContext gc, Point2D playerCenter, Character player) {

        if (player.getCurrentAction() == Action.IDLE) {

            // transform
            long angle = (long)player.getPlayerAngle().getAngle();
            ISOConverter.applyAngleRotation(gc, angle);

            // draw
            gc.setFill(Color.RED);
            int cursorRadius = 10;
            gc.fillOval(playerCenter.getX() - cursorRadius/2f, playerCenter.getY() - cursorRadius/2f - player.getWidth()/2f - 30, cursorRadius, cursorRadius);

            gc.restore();
        }

    }


}
