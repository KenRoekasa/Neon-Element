package graphics;


import entities.Player;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;

import static javafx.scene.transform.Rotate.X_AXIS;

public class ISOConverter {


    public static Point2D isoTo2D(Point2D point) {
        double x = (2 * point.getY() + point.getX()) / 2;
        double y = (2 * point.getY()- point.getX()) / 2;
        return new Point2D(x, y);
    }

    static Point2D twoDToIso(Point2D point) {
        double x = point.getX()- point.getY();
        double y = (point.getX() + point.getY()) / 2;
        return new Point2D(x,y);
    }


    static void applyRotationTransform(GraphicsContext gc, Point2D playerCenter, long angle) {
        gc.save();
        Affine affine = new Affine();

        Rotate rotateX = new Rotate(45, playerCenter.getX(), playerCenter.getY());
        Rotate rotateZ = new Rotate(60.0, playerCenter.getX(), playerCenter.getY(), 0, X_AXIS);
        affine.prependRotation(angle, playerCenter.getX(), playerCenter.getY());
        affine.prepend(rotateX);
        affine.prepend(rotateZ);
        gc.transform(affine);
    }

}
