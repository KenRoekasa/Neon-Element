package graphics.rendering;

import engine.entities.Character;
import graphics.enumSwitches.colourSwitch;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.Effect;
import javafx.scene.effect.MotionBlur;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;


class DrawStates {

    private static Color shieldStartColour = new Color(Color.LIGHTSTEELBLUE.getRed(), Color.LIGHTSTEELBLUE.getGreen(), Color.LIGHTSTEELBLUE.getBlue(), 0.5);
    private static Color shieldStopColour = new Color(Color.BLUE.getRed(), Color.BLUE.getGreen(), Color.BLUE.getBlue(), 0.5);


    static void drawHeavyAttack(GraphicsContext gc, Character player, long remainingAnimDuration, long animationDuration, Point2D playerCenter) {
        long startAngle = 0;
        long finishAngle = 180;
        long angle = Renderer.mapInRange(remainingAnimDuration, 0, animationDuration, startAngle, finishAngle);

        gc.save();
        gc.setFill(colourSwitch.getElementColour(player.getCurrentElement()));
        gc.setEffect(new MotionBlur(0, 3));

        ISOConverter.applyAngleRotation(gc, angle, playerCenter);
        gc.strokeLine(playerCenter.getX() - 20, playerCenter.getY() - 20, playerCenter.getX() - 100, playerCenter.getY() - 100);

        ISOConverter.applyAngleRotation(gc, 90, playerCenter);
        gc.strokeLine(playerCenter.getX() - 20, playerCenter.getY() - 20, playerCenter.getX() - 100, playerCenter.getY() - 100);

        ISOConverter.applyAngleRotation(gc, 90, playerCenter);
        gc.strokeLine(playerCenter.getX() - 20, playerCenter.getY() - 20, playerCenter.getX() - 100, playerCenter.getY() - 100);

        ISOConverter.applyAngleRotation(gc, 90, playerCenter);
        gc.strokeLine(playerCenter.getX() - 20, playerCenter.getY() - 20, playerCenter.getX() - 100, playerCenter.getY() - 100);


        gc.restore();
    }

    static void drawHeavyAttackCharge(GraphicsContext gc, Character player, long remainingAnimDuration, long animationDuration, Point2D playerCenter) {
        long startAlphaValue = 0;
        long finishAlphaValue = 80;
        float percentCharged = (Renderer.mapInRange(remainingAnimDuration, 0, animationDuration, finishAlphaValue, startAlphaValue)) / 100f;
        //todo get this from player class
        double attackRadius = 300;

        gc.save();
        gc.setFill(colourSwitch.getElementColour(player.getCurrentElement()));

        //todo make better

        //inner
        gc.setGlobalAlpha(percentCharged);
        gc.fillOval(playerCenter.getX() - attackRadius / 2f, playerCenter.getY() - attackRadius / 2f, attackRadius, attackRadius);


        gc.restore();

    }

    static void drawLightAttack(GraphicsContext gc, Character player, long remainingAnimDuration, long animationDuration, Point2D playerCenter) {
        long startAngle = 0;
        long finishAngle = 90;

        long angle = (long) (player.getPlayerAngle().getAngle() + Renderer.mapInRange(remainingAnimDuration, 0, animationDuration, startAngle, finishAngle));

        gc.save();
        Effect lightEffect = new MotionBlur(0, 3);
        gc.setEffect(lightEffect);

        ISOConverter.applyAngleRotation(gc, angle, playerCenter);
        gc.strokeLine(playerCenter.getX() - 20, playerCenter.getY() - 20, playerCenter.getX() - 100, playerCenter.getY() - 100);

        ISOConverter.applyAngleRotation(gc, 1 , playerCenter);
        gc.strokeLine(playerCenter.getX() - 20, playerCenter.getY() - 20, playerCenter.getX() - 100, playerCenter.getY() - 100);

        ISOConverter.applyAngleRotation(gc, 1, playerCenter);
        gc.strokeLine(playerCenter.getX() - 20, playerCenter.getY() - 20, playerCenter.getX() - 100, playerCenter.getY() - 100);

        // restore for each ISO conversion
        gc.restore();

    }

    static void drawShield(GraphicsContext gc, Character player, Point2D playerCenter) {
        gc.save();
        double shieldWidth = player.getWidth() + 20;

        // this creates the gradient needed
        // using custom colours with lowered opacity as gc.setGlobalAlpha was causing me issues making everything transparent
        Stop[] stops1 = new Stop[]{new Stop(0.25, shieldStartColour), new Stop(1, shieldStopColour)};
        RadialGradient lg1 = new RadialGradient(0, 0, 0.5, 0.5, 0.8, true, CycleMethod.NO_CYCLE, stops1);

        gc.setFill(lg1);

        gc.fillOval(playerCenter.getX() - shieldWidth / 2, playerCenter.getY() - shieldWidth / 2, shieldWidth, shieldWidth);
        gc.restore();

     }

}
