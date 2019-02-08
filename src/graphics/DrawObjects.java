package graphics;

import entities.Player;
import entities.PowerUp;
import enumSwitches.colourSwitch;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import static graphics.Renderer.getRelativeLocation;

class DrawObjects {
    static void drawMap(GraphicsContext gc, Rectangle stage, Rectangle map, Player player) {


        Point2D stageCenter = new Point2D(stage.getWidth() / 2, stage.getHeight() / 2);
        Point2D playerLocation = player.getLocation();

        double relativeX = stageCenter.getX() - playerLocation.getX() ;
        double relativeY = stageCenter.getY() - playerLocation.getY();
        Point2D boardPosition = new Point2D(relativeX, relativeY);

        // draw map
        gc.setFill(Color.rgb(214, 214,214));
        gc.fillRect(boardPosition.getX(), boardPosition.getY(), map.getWidth(), map.getHeight());
        gc.strokeRect(boardPosition.getX(), boardPosition.getY(), map.getWidth(), map.getHeight());

        // restore previous state

    }

    static void drawPowerUp(GraphicsContext gc, Rectangle stage, PowerUp powerUp, Player player) {

        Point2D relativeLocation = getRelativeLocation(stage, powerUp, player);

        Color c = colourSwitch.getPowerUpColour(powerUp.getType());
        gc.setFill(c);

        gc.fillOval(relativeLocation.getX(), relativeLocation.getY(), powerUp.getWidth(), powerUp.getWidth());

    }

    static void drawCrosshair(GraphicsContext gc, Rectangle stage) {
        gc.strokeLine(stage.getWidth() / 2, 0, stage.getWidth() / 2, stage.getHeight());
        gc.strokeLine(0, stage.getHeight() / 2, stage.getWidth(), stage.getHeight() / 2);
    }

    public static void drawBackground(GraphicsContext gc, Rectangle stageSize) {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, stageSize.getWidth(), stageSize.getHeight());
    }
}
