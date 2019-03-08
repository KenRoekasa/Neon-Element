package graphics.rendering;


import engine.entities.PhysicsObject;
import engine.entities.Player;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

import static graphics.rendering.Renderer.xOffset;
import static graphics.rendering.Renderer.yOffset;
import static javafx.scene.transform.Rotate.X_AXIS;

public class ISOConverter {

    public static Point2D isoTo2D(Point2D point) {
        double x = (2 * point.getY() + point.getX()) / 2;
        double y = (2 * point.getY()- point.getX()) / 2;
        return new Point2D(x, y);
    }

    public static Point2D twoDToIso(Point2D point) {
        double x = point.getX()- point.getY();
        double y = (point.getX() + point.getY()) / 2;

        return new Point2D(x,y);
    }

    public static Point2D getLocationOnScreen(Point2D relativeLocation, PhysicsObject object, Rectangle stage){
        Rectangle r = new Rectangle(relativeLocation.getX(), relativeLocation.getY(), object.getWidth(), object.getWidth());
        r.getTransforms().add(ISOConverter.getTransformationAffine(new Point2D(stage.getWidth()/2f, stage.getHeight()/2f)));

        Point2D newLoc = r.localToParent(relativeLocation.getX(), relativeLocation.getY());

        return  newLoc;
    }

    public static Point2D getPlayerLocOnScreen(Player player, Rectangle primaryStage) {
        Point2D playerLocation = new Point2D(primaryStage.getWidth()/2 + xOffset, primaryStage.getHeight()/2 + yOffset);
        Rectangle r = new Rectangle(playerLocation.getX(), playerLocation.getY(), player.getWidth(), player.getWidth());
        r.getTransforms().add(ISOConverter.getTransformationAffine(new Point2D(primaryStage.getWidth()/2f, primaryStage.getHeight()/2f)));
        return r.localToParent(playerLocation.getX(), playerLocation.getY());
    }


    static void applyRotationTransform(GraphicsContext gc, Point2D playerCenter) {
        Affine a = getTransformationAffine(playerCenter);
        gc.transform(a);
    }

    public static Affine getTransformationAffine(Point2D playerCenter){
        Affine affine = new Affine();

        Rotate rotateX = new Rotate(45, playerCenter.getX(), playerCenter.getY());
        Rotate rotateZ = new Rotate(60.0, playerCenter.getX(), playerCenter.getY(), 0, X_AXIS);

        affine.prepend(rotateX);
        affine.prepend(rotateZ);

        return affine;
    }


    static void applyAngleRotation(GraphicsContext gc, long angle, Point2D rotationCenter) {

        Affine affine = new Affine();
        affine.prependRotation(angle, rotationCenter.getX(), rotationCenter.getY());
        gc.transform(affine);
    }


}
