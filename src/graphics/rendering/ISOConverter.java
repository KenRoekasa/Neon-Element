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

/**
 * Contains a number of methods used for converting between cartesian and isometric coordinates
 */
public class ISOConverter {


    /**
     * Gets the location of an object on the screen
     * @param relativeLocation The objects location in regards to the client player
     * @param object    The object
     * @param stageSize The size of the stage being drawn on
     * @return  The location of the object on the screen
     */
    public static Point2D getLocationOnScreen(Point2D relativeLocation, PhysicsObject object, Rectangle stageSize){
        Rectangle r = new Rectangle(relativeLocation.getX(), relativeLocation.getY(), object.getWidth(), object.getWidth());
        r.getTransforms().add(ISOConverter.getTransformationAffine(new Point2D(stageSize.getWidth()/2f, stageSize.getHeight()/2f)));

        Point2D newLoc = r.localToParent(relativeLocation.getX(), relativeLocation.getY());

        return  newLoc;
    }

    /**
     * Gets the location of the client player on the screen
     * @param player The client player
     * @param stageSize The size of the stage being drawn on
     * @return  The location of the player on the screen
     */
    public static Point2D getPlayerLocOnScreen(Player player, Rectangle stageSize) {
        Point2D playerLocation = new Point2D(stageSize.getWidth()/2 + xOffset, stageSize.getHeight()/2 + yOffset);
        Rectangle r = new Rectangle(playerLocation.getX(), playerLocation.getY(), player.getWidth(), player.getWidth());
        r.getTransforms().add(ISOConverter.getTransformationAffine(new Point2D(stageSize.getWidth()/2f, stageSize.getHeight()/2f)));
        return r.localToParent(playerLocation.getX(), playerLocation.getY());
    }


    /**
     * Applies the isometric transformation to the graphics context
     * @param gc The graphics context
     * @param rotationCenter  The point around which the transform takes place
     */
    static void applyRotationTransform(GraphicsContext gc, Point2D rotationCenter) {
        Affine a = getTransformationAffine(rotationCenter);
        gc.transform(a);
    }

    /**
     * Returns the isometric transformation
     * @param rotationCenter The center of rotation
     * @return  The isometric conversion affine Affine
     */
    private static Affine getTransformationAffine(Point2D rotationCenter){
        Affine affine = new Affine();

        Rotate rotateX = new Rotate(45, rotationCenter.getX(), rotationCenter.getY());
        Rotate rotateZ = new Rotate(60.0, rotationCenter.getX(), rotationCenter.getY(), 0, X_AXIS);

        affine.prepend(rotateX);
        affine.prepend(rotateZ);

        return affine;
    }


    /**
     * Applies a specific rotation to the GraphicsContext
     * @param gc The GraphicsContext to transform
     * @param angle The angle to rotate by
     * @param rotationCenter    The center of rotation
     */
    public static void applyAngleRotation(GraphicsContext gc, long angle, Point2D rotationCenter) {

        Affine affine = new Affine();
        affine.prependRotation(angle, rotationCenter.getX(), rotationCenter.getY());
        gc.transform(affine);
    }


}
