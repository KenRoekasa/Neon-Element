package graphics;

import entities.Player;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;

import static javafx.scene.transform.Rotate.X_AXIS;

public class DrawPlayers {
    static void drawPlayer(GraphicsContext gc, Rectangle stage, Player player, double scaleConstant) {

        // Point2D isoLocation = ISOConverter.twoDToIso(playerLocation);
        Point2D stageCenter = new Point2D(stage.getWidth() / 2, stage.getHeight() / 2);

        double playerXCenter = stageCenter.getX();
        double playerYCenter = stageCenter.getY();

        ISOConverter.applyIsoTransform(gc, playerXCenter, playerYCenter);

        Color c = ElementColourSwitch.getColour(player.getCurrentElement());
        gc.setFill(c);

        double centerAdjust = player.getWidth()/2f * scaleConstant;

        gc.fillRect(playerXCenter - centerAdjust, playerYCenter - centerAdjust, player.getWidth() * scaleConstant, player.getWidth()* scaleConstant);

        // has to be called after applying transform
        gc.restore();


    }

    @SuppressWarnings("Duplicates")
    static void drawerCursor(GraphicsContext gc, Rectangle stage, Player player) {

        int cursorRadius = 10;

        Point2D stageCenter = new Point2D(stage.getWidth() / 2, stage.getHeight() / 2);
        double playerXCenter = stageCenter.getX();
        double playerYCenter = stageCenter.getY();

        gc.save();
        Affine affine = new Affine();

        affine.prependRotation(player.getPlayerAngle().getAngle(), playerXCenter, playerYCenter);

        Rotate rotateX = new Rotate(45, playerXCenter, playerYCenter);
        Rotate rotateZ = new Rotate(60.0, playerXCenter, playerYCenter, 0, X_AXIS);
        affine.prepend(rotateX);
        affine.prepend(rotateZ);
        gc.transform(affine);
        gc.setFill(Color.RED);

        gc.fillOval(playerXCenter - cursorRadius/2f, playerYCenter - cursorRadius/2f - 30, cursorRadius, cursorRadius);

        gc.restore();
    }



}
