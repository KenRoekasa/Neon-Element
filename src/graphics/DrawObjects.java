package graphics;

import entities.Player;
import entities.PowerUp;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Rectangle;

public class DrawObjects {
    static void drawMap(GraphicsContext gc, Rectangle stage, Rectangle map, Player player, double scaleConstant) {

        Point2D stageCenter = new Point2D(stage.getWidth() / 2, stage.getHeight() / 2);
        Point2D isoPlayerLocation = ISOConverter.twoDToIso(player.getLocation());
        isoPlayerLocation = isoPlayerLocation.add(10, 0);

        double relativeX = stageCenter.getX() - isoPlayerLocation.getX() + player.getWidth() / 2f;
        double relativeY = stageCenter.getY() - isoPlayerLocation.getY();
        Point2D boardPosition = new Point2D(relativeX, relativeY);

        ISOConverter.applyIsoTransform(gc, boardPosition.getX(), boardPosition.getY());

        // draw map
        gc.strokeRect(boardPosition.getX(), boardPosition.getY(), map.getWidth() * scaleConstant, map.getHeight() * scaleConstant);

        // restore previous state
        gc.restore();

    }

    @SuppressWarnings("Duplicates")
    static void drawPowerUp(GraphicsContext gc, Rectangle stage, PowerUp powerUp, Player player, double scaleConstant) {


        Point2D powerUpLocation = ISOConverter.twoDToIso(powerUp.getLocation());
        Point2D isoPlayerLocation = ISOConverter.twoDToIso(player.getLocation());

        double relativeX = stage.getWidth() / 2f - isoPlayerLocation.getX() + powerUpLocation.getX();
        double relativeY = stage.getHeight() / 2f - isoPlayerLocation.getY() + powerUpLocation.getY();

        Point2D relativeLocation = new Point2D(relativeX, relativeY);

        ISOConverter.applyIsoTransform(gc, relativeLocation.getX(), relativeLocation.getY());

        gc.fillOval(relativeLocation.getX(), relativeLocation.getY(), powerUp.getWidth() * scaleConstant, powerUp.getWidth() * scaleConstant);

        gc.restore();

    }


    static void drawCrosshair(GraphicsContext gc, Rectangle stage) {
        gc.strokeLine(stage.getWidth() / 2, 0, stage.getWidth() / 2, stage.getHeight());
        gc.strokeLine(0, stage.getHeight() / 2, stage.getWidth(), stage.getHeight() / 2);
    }
}
