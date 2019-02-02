package graphics;

import entities.Enemy;
import entities.Player;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;

import static javafx.scene.transform.Rotate.X_AXIS;

public class DrawEnemies {
    @SuppressWarnings("Duplicates")
    static void drawEnemy(GraphicsContext gc, Rectangle stage, Enemy enemy, Player player, double scaleConstant) {

        // calculates graphical location of object based on its location on board and players location on board
        Point2D enemyLocation = ISOConverter.twoDToIso(enemy.getLocation());
        Point2D isoPlayerLocation = ISOConverter.twoDToIso(player.getLocation());

        double relativeX = stage.getWidth() / 2f - isoPlayerLocation.getX() + enemyLocation.getX();
        double relativeY = stage.getHeight() / 2f - isoPlayerLocation.getY() + enemyLocation.getY() - enemy.getWidth() / 2f;
        Point2D relativeLocation = new Point2D(relativeX, relativeY);

        ISOConverter.applyIsoTransform(gc, relativeLocation.getX(), relativeLocation.getY());

        Color c = ElementColourSwitch.getColour(enemy.getCurrentElement());
        gc.setFill(c);

        //draw enemy
        gc.fillRect(relativeLocation.getX(), relativeLocation.getY(), player.getWidth() * scaleConstant, player.getWidth() * scaleConstant);

        // restore prev state
        gc.restore();
    }

    @SuppressWarnings("Duplicates")
    static void drawerEnemyCursor(GraphicsContext gc, Rectangle stage, Enemy enemy, Player player) {

        int cursorRadius = 10;

        Point2D enemyLocation = ISOConverter.twoDToIso(enemy.getLocation());
        Point2D isoPlayerLocation = ISOConverter.twoDToIso(player.getLocation());

        double relativeX = stage.getWidth() / 2f - isoPlayerLocation.getX() + enemyLocation.getX();
        double relativeY = stage.getHeight() / 2f - isoPlayerLocation.getY() + enemyLocation.getY();
        Point2D relativeLocation = new Point2D(relativeX, relativeY);


        gc.save();
        Affine affine = new Affine();

        affine.prependRotation(enemy.getPlayerAngle().getAngle(), relativeLocation.getX(), relativeLocation.getY());

        Rotate rotateX = new Rotate(45, relativeLocation.getX(), relativeLocation.getY());
        Rotate rotateZ = new Rotate(60.0, relativeLocation.getX(), relativeLocation.getY(), 0, X_AXIS);
        affine.prepend(rotateX);
        affine.prepend(rotateZ);
        gc.transform(affine);
        gc.setFill(Color.RED);

        gc.fillOval(relativeLocation.getX() - cursorRadius / 2f, relativeLocation.getY() - cursorRadius / 2f - 30, cursorRadius, cursorRadius);

        gc.restore();
    }
}
