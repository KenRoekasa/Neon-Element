package Grid;


import javafx.geometry.Point2D;

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

}
