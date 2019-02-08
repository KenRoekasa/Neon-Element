package graphics;

import entities.Character;
import enumSwitches.colourSwitch;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;


class DrawAttacks {

    static void drawHeavyAttack(GraphicsContext gc, Character player, long remainingAnimDuration, long animationDuration, Point2D playerCenter) {
        long startAngle = 0;
        long finishAngle = 360;
        long angle = Renderer.mapInRange(remainingAnimDuration, 0, animationDuration, startAngle, finishAngle);

        ISOConverter.applyAngleRotation(gc, angle);


        gc.setFill(colourSwitch.getElementColour(player.getCurrentElement()));

        gc.strokeLine(playerCenter.getX() - 20, playerCenter.getY() - 20, playerCenter.getX() - 100, playerCenter.getY() - 100);
        //todo make better

        gc.restore();
    }

    static void drawHeavyAttackCharge(GraphicsContext gc, Character player, long remainingAnimDuration, long animationDuration, Point2D playerCenter) {
        long startAlphaValue = 0;
        long finishAlphaValue = 80;
        float percentCharged = (Renderer.mapInRange(remainingAnimDuration, 0, animationDuration, finishAlphaValue, startAlphaValue)) / 100f;
        //todo get this from player class
        double attackRadius = 200;



        gc.setFill(colourSwitch.getElementColour(player.getCurrentElement()));

        //gc.strokeLine(stageCenter.getX() - 20, stageCenter.getY() - 20, stageCenter.getX() - 100, stageCenter.getY() - 100);
        //todo make better

        //inner
        gc.setGlobalAlpha(percentCharged);
        gc.fillOval(playerCenter.getX() - attackRadius/2f, playerCenter.getY() - attackRadius/2f, attackRadius, attackRadius);

        //border
        gc.setGlobalAlpha(100);
        gc.strokeOval(playerCenter.getX() - attackRadius/2f, playerCenter.getY() - attackRadius/2f, attackRadius, attackRadius);


    }

    static void drawLightAttack(GraphicsContext gc, Character player, long remainingAnimDuration, long animationDuration, Point2D playerCenter) {
        long startAngle = 0;
        long finishAngle = 90;

        long angle = (long) (player.getPlayerAngle().getAngle() + Renderer.mapInRange(remainingAnimDuration, 0, animationDuration, startAngle, finishAngle));

        ISOConverter.applyAngleRotation(gc, angle);
        gc.setFill(Color.BLUE);

        //todo make better
        //gc.fillOval(playerCenter.getX() - 10/2f, playerCenter.getY() - 10/2f - player.getWidth()/2f - 30, 10, 10);
        gc.strokeLine(playerCenter.getX() - 20, playerCenter.getY() - 20, playerCenter.getX() - 100, playerCenter.getY() - 100);


        gc.restore();
    }

}
