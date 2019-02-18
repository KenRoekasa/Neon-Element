package graphics.rendering;

import engine.entities.Character;
import graphics.enumSwitches.colourSwitch;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;


class DrawStates {

    static void drawHeavyAttack(GraphicsContext gc, Character player, long remainingAnimDuration, long animationDuration, Point2D playerCenter) {
        long startAngle = 0;
        long finishAngle = 360;
        long angle = Renderer.mapInRange(remainingAnimDuration, 0, animationDuration, startAngle, finishAngle);

        ISOConverter.applyAngleRotation(gc, angle, playerCenter);

        gc.save();
        gc.setFill(colourSwitch.getElementColour(player.getCurrentElement()));

        gc.strokeLine(playerCenter.getX() - 20, playerCenter.getY() - 20, playerCenter.getX() - 100, playerCenter.getY() - 100);
        //todo make better

        gc.restore();
        gc.restore();
    }

    static void drawHeavyAttackCharge(GraphicsContext gc, Character player, long remainingAnimDuration, long animationDuration, Point2D playerCenter) {
        long startAlphaValue = 0;
        long finishAlphaValue = 80;
        float percentCharged = (Renderer.mapInRange(remainingAnimDuration, 0, animationDuration, finishAlphaValue, startAlphaValue)) / 100f;
        //todo get this from player class
        double attackRadius = 200;

        gc.save();
        gc.setFill(colourSwitch.getElementColour(player.getCurrentElement()));

        //gc.strokeLine(stageCenter.getX() - 20, stageCenter.getY() - 20, stageCenter.getX() - 100, stageCenter.getY() - 100);
        //todo make better

        //inner
        gc.setGlobalAlpha(percentCharged);
        gc.fillOval(playerCenter.getX() - attackRadius / 2f, playerCenter.getY() - attackRadius / 2f, attackRadius, attackRadius);

        //border
        gc.setGlobalAlpha(100);
        gc.strokeOval(playerCenter.getX() - attackRadius / 2f, playerCenter.getY() - attackRadius / 2f, attackRadius, attackRadius);
        gc.restore();

    }

    static void drawLightAttack(GraphicsContext gc, Character player, long remainingAnimDuration, long animationDuration, Point2D playerCenter) {
        long startAngle = 0;
        long finishAngle = 90;

        long angle = (long) (player.getPlayerAngle().getAngle() + Renderer.mapInRange(remainingAnimDuration, 0, animationDuration, startAngle, finishAngle));

        ISOConverter.applyAngleRotation(gc, angle, playerCenter);
        gc.save();
        gc.setFill(Color.BLUE);

        //todo make better
        //gc.fillOval(playerCenter.getX() - 10/2f, playerCenter.getY() - 10/2f - player.getWidth()/2f - 30, 10, 10);
        gc.strokeLine(playerCenter.getX() - 20, playerCenter.getY() - 20, playerCenter.getX() - 100, playerCenter.getY() - 100);

        gc.restore();
        gc.restore();
    }

    static void drawShield(GraphicsContext gc, Character player, Point2D playerCenter) {
        double shieldWidth = player.getWidth() + 20;


        Stop[] stops1 = new Stop[]{new Stop(0, Color.LIGHTSTEELBLUE), new Stop(1, Color.BLUE)};
        RadialGradient lg1 = new RadialGradient(0, 0, 0.5, 0.5, 0.8, true, CycleMethod.NO_CYCLE, stops1);
        gc.save();
        gc.setFill(lg1);
        gc.setGlobalAlpha(0.5);

        gc.fillOval(playerCenter.getX() - shieldWidth / 2, playerCenter.getY() - shieldWidth / 2, shieldWidth, shieldWidth);
        gc.restore();
    }


}
