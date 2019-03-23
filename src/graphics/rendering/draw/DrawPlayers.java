package graphics.rendering.draw;

import engine.entities.Character;

import engine.entities.Player;
import engine.model.enums.Action;
import graphics.rendering.ISOConverter;
import graphics.rendering.Renderer;
import graphics.rendering.colourSwitch;
import graphics.rendering.textures.Sprites;
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


        Effect glow = new Glow(0.8);    // this gives a nice "neon" glow to all of the players

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
            long angle = (long)player.getPlayerAngle().getAngle();

            gc.save();
            ISOConverter.applyAngleRotation(gc, angle, playerCenter);

            gc.drawImage(Renderer.textures.get(Sprites.POINTER),playerCenter.getX() - Renderer.textures.get(Sprites.POINTER).getWidth()/2f, playerCenter.getY() - Renderer.textures.get(Sprites.POINTER).getHeight()/2f - player.getWidth()/2f - 30 );

            //restore twice following applying the specific angle rotation
            gc.restore();
        }

    }


}
