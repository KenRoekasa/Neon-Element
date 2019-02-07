package graphics;

import entities.Player;
import entities.PowerUp;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import static graphics.Renderer.getRelativeLocation;

class DrawObjects {
    static void drawMap(GraphicsContext gc, Rectangle stage, Rectangle map, Player player) {
        double scaleConstant = Renderer.getScaleConstant();

        Point2D stageCenter = new Point2D(stage.getWidth() / 2, stage.getHeight() / 2);
        Point2D isoPlayerLocation = ISOConverter.twoDToIso(player.getLocation());
        isoPlayerLocation = isoPlayerLocation.add(10, 0);

        double relativeX = stageCenter.getX() - isoPlayerLocation.getX() + player.getWidth() / 2f;
        double relativeY = stageCenter.getY() - isoPlayerLocation.getY();
        Point2D boardPosition = new Point2D(relativeX, relativeY);

        ISOConverter.applyRotationTransform(gc, boardPosition, 0);

        // draw map
        gc.strokeRect(boardPosition.getX(), boardPosition.getY(), map.getWidth() * scaleConstant, map.getHeight() * scaleConstant);

        // restore previous state
        gc.restore();

    }


    static void drawPowerUp(GraphicsContext gc, Rectangle stage, PowerUp powerUp, Player player) {

        double scaleConstant = Renderer.getScaleConstant();

        // get width offset
        double yOffset = - powerUp.getWidth();
        Point2D relativeLocation = getRelativeLocation(stage, powerUp, player, yOffset);

        ISOConverter.applyRotationTransform(gc, relativeLocation, 0);

        Color c = EnumColourSwitch.getPowerUpColour(powerUp.getType());
        gc.setFill(c);

        gc.fillOval(relativeLocation.getX(), relativeLocation.getY(), powerUp.getWidth() * scaleConstant, powerUp.getWidth() * scaleConstant);

        gc.restore();

    }


    static void drawCrosshair(GraphicsContext gc, Rectangle stage) {
        gc.strokeLine(stage.getWidth() / 2, 0, stage.getWidth() / 2, stage.getHeight());
        gc.strokeLine(0, stage.getHeight() / 2, stage.getWidth(), stage.getHeight() / 2);
    }
}
