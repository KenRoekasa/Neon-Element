package graphics.rendering;

import engine.entities.Character;

import engine.entities.Player;
import graphics.enumSwitches.colourSwitch;
import engine.enums.Action;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Glow;
import javafx.scene.effect.MotionBlur;
import javafx.scene.paint.Color;

class DrawPlayers {


    static void drawPlayer(GraphicsContext gc, Point2D playerCenter, Character player) {
        gc.save();
        Color c = colourSwitch.getElementColour(player.getCurrentElement());


        Effect glow = new Glow(0.4);    // this gives a nice "neon" glow to all of the players

        if(player.getMovementSpeed() > Player.DEFAULT_MOVEMENT_SPEED) {

            MotionBlur m = new MotionBlur(0, 10);
            m.setInput(glow);
            
            gc.setEffect(m);
        } else {
            gc.setEffect(glow);
        }

        gc.setFill(c);
        gc.fillRect(playerCenter.getX(), playerCenter.getY(), player.getWidth(), player.getWidth());
        gc.restore();
    }

    static void drawCursor(GraphicsContext gc, Point2D playerCenter, Character player) {

        if (player.getCurrentAction() == Action.IDLE) {
            // transform
            long angle = (long)player.getPlayerAngle().getAngle();
            ISOConverter.applyAngleRotation(gc, angle, playerCenter);

            // draw

            gc.save();
            gc.setFill(Color.RED);
            int cursorRadius = 10;
            gc.fillOval(playerCenter.getX() - cursorRadius/2f, playerCenter.getY() - cursorRadius/2f - player.getWidth()/2f - 30, cursorRadius, cursorRadius);
            //restore twice following applying the specific angle rotation
            gc.restore();
            gc.restore();
        }

    }


}
