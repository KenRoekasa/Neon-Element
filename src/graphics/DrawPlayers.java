package graphics;

import entities.Character;
import entities.PhysicsObject;
import entities.Player;
import enums.Action;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.awt.*;

public class DrawPlayers {
    static void drawPlayer(GraphicsContext gc, Point2D playerCenter, Character player) {

        double scaleConstant = Renderer.getScaleConstant();

        ISOConverter.applyRotationTransform(gc, playerCenter, 0);

        Color c = EnumColourSwitch.getElementColour(player.getCurrentElement());
        gc.setFill(c);

        gc.fillRect(playerCenter.getX(), playerCenter.getY(), player.getWidth() * scaleConstant, player.getWidth()* scaleConstant);

        // has to be called after applying transform
        gc.restore();

    }

    static void drawCursor(GraphicsContext gc, Point2D playerCenter, Character player) {

        if (player.getCurrentAction() == Action.IDLE) {

            // transform
            long angle = (long)player.getPlayerAngle().getAngle();
            ISOConverter.applyRotationTransform(gc, playerCenter, angle);

            // draw
            gc.setFill(Color.RED);
            int cursorRadius = 10;
            gc.fillOval(playerCenter.getX() - cursorRadius/2f, playerCenter.getY() - cursorRadius/2f - 30, cursorRadius, cursorRadius);

            gc.restore();
        }

    }


}
