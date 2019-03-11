package engine.entities;

import engine.enums.ObjectType;
import javafx.geometry.Point2D;

public class Wall extends PhysicsObject {

    private Wall() {
        tag = ObjectType.OBSTACLE;
    }

    public Wall(Point2D location, int width, int height) {
        this();
        this.height = height;
        this.width = width;
        this.location = location.add(width/2f,height/2f);

    }

    public Wall(double x, double y, int width, int height) {
        this();
        this.height = height;
        this.width = width;
        this.location = new Point2D(x+width/2f, y+height/2f);
    }


    @Override
    public void update() {

    }
}
