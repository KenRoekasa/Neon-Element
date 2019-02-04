package graphics;

import entities.Player;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;

import static javafx.scene.transform.Rotate.X_AXIS;

public class DrawPlayers {
    static void drawPlayer(GraphicsContext gc, Rectangle stage, Player player, double scaleConstant) {

        // Point2D isoLocation = ISOConverter.twoDToIso(playerLocation);
        Point2D stageCenter = getStageCenter(stage);

        ISOConverter.applyIsoTransform(gc, stageCenter.getX(), stageCenter.getY());

        Color c = EnumColourSwitch.getElementColour(player.getCurrentElement());
        gc.setFill(c);

        double centerAdjust = player.getWidth()/2f * scaleConstant;

        gc.fillRect(stageCenter.getX() - centerAdjust, stageCenter.getY() - centerAdjust, player.getWidth() * scaleConstant, player.getWidth()* scaleConstant);

        // has to be called after applying transform
        gc.restore();


    }

    @SuppressWarnings("Duplicates")
    static void drawerCursor(GraphicsContext gc, Rectangle stage, Player player) {

        int cursorRadius = 10;
        Point2D stageCenter = getStageCenter(stage);
        
        gc.save();
        Affine affine = new Affine();

        affine.prependRotation(player.getPlayerAngle().getAngle(), stageCenter.getX(), stageCenter.getY());

        Rotate rotateX = new Rotate(45, stageCenter.getX(), stageCenter.getY());
        Rotate rotateZ = new Rotate(60.0, stageCenter.getX(), stageCenter.getY(), 0, X_AXIS);
        affine.prepend(rotateX);
        affine.prepend(rotateZ);
        gc.transform(affine);
        gc.setFill(Color.RED);

        gc.fillOval(stageCenter.getX() - cursorRadius/2f, stageCenter.getY() - cursorRadius/2f - 30, cursorRadius, cursorRadius);

        gc.restore();
    }


    static void drawLightAttack(GraphicsContext gc, Player player, long remainingAnimDuration, long animationDuration, Rectangle stage) {

        Point2D stageCenter = getStageCenter(stage);
        long startAngle = 0;
        long finishAngle = 90;

        long angle = (long) (player.getPlayerAngle().getAngle() - finishAngle/2 + Renderer.mapInRange(remainingAnimDuration, 0, animationDuration, startAngle, finishAngle));


        gc.save();
        Affine affine = new Affine();

        affine.prependRotation(angle, stageCenter.getX(), stageCenter.getY());

        Rotate rotateX = new Rotate(45, stageCenter.getX(), stageCenter.getY());
        Rotate rotateZ = new Rotate(60.0, stageCenter.getX(), stageCenter.getY(), 0, X_AXIS);
        affine.prepend(rotateX);
        affine.prepend(rotateZ);
        gc.transform(affine);
        gc.setFill(Color.BLUE);

        //gc.strokeLine(stageCenter.getX() - 20, stageCenter.getY() - 20, stageCenter.getX() - 100, stageCenter.getY() - 100);
        //todo make better
        gc.fillOval(stageCenter.getX() - 10/2f, stageCenter.getY() - 10/2f - 30, 10, 10);

        gc.restore();
    }

    static void drawHeavyAttackCharge(GraphicsContext gc, Player player, long remainingAnimDuration, long animationDuration, Rectangle stage) {

        Point2D stageCenter = getStageCenter(stage);

        long startAlphaValue = 0;
        long finishAlphaValue = 100;
        float percentCharged = (Renderer.mapInRange(remainingAnimDuration, 0, animationDuration, finishAlphaValue, startAlphaValue)) / 100f;
        double attackRadius = 200 * Renderer.getScaleConstant();

        gc.save();
        Affine affine = new Affine();

        Rotate rotateX = new Rotate(45, stageCenter.getX(), stageCenter.getY());
        Rotate rotateZ = new Rotate(60.0, stageCenter.getX(), stageCenter.getY(), 0, X_AXIS);
        affine.prepend(rotateX);
        affine.prepend(rotateZ);
        gc.transform(affine);
        gc.setFill(EnumColourSwitch.getElementColour(player.getCurrentElement()));

        //gc.strokeLine(stageCenter.getX() - 20, stageCenter.getY() - 20, stageCenter.getX() - 100, stageCenter.getY() - 100);
        //todo make better

        //inner
        gc.setGlobalAlpha(percentCharged);
        gc.fillOval(stageCenter.getX() - attackRadius/2f, stageCenter.getY() - attackRadius/2f, attackRadius, attackRadius);

        //border
        gc.setGlobalAlpha(100);
        gc.strokeOval(stageCenter.getX() - attackRadius/2f, stageCenter.getY() - attackRadius/2f, attackRadius, attackRadius);

        //affine.prependRotation(angle, stageCenter.getX(), stageCenter.getY());
        gc.restore();
    }


    private static Point2D getStageCenter(Rectangle stageSize) {
        Point2D stageCenter = new Point2D(stageSize.getWidth() / 2, stageSize.getHeight() / 2);
        return stageCenter;
    }
}
