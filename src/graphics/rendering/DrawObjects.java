package graphics.rendering;

import engine.entities.PhysicsObject;
import engine.entities.Player;
import engine.entities.PowerUp;
import graphics.enumSwitches.colourSwitch;
import graphics.enums.UIColour;
import graphics.rendering.textures.Sprites;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.*;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

import static graphics.rendering.Renderer.getRelativeLocation;

class DrawObjects {

    private static Stop[] stops1 = new Stop[]{new Stop(0.2, Color.web("060419")), new Stop(0.4, Color.web("041830")), new Stop(0.7, Color.ORCHID), new Stop(1, Color.ORANGE)};
    private static RadialGradient lg1 = new RadialGradient(0, 0, 0.5, 0.5, 0.8, true, CycleMethod.NO_CYCLE, stops1);


    static void drawMap(GraphicsContext gc, Rectangle stage, Rectangle map, Player player, HashMap<Sprites, Image> textures) {

        Point2D stageCenter = new Point2D(stage.getWidth() / 2, stage.getHeight() / 2);
        Point2D playerLocation = player.getLocation();

//        double relativeX = stageCenter.getX() - playerLocation.getX() ;
//        double relativeY = stageCenter.getY() - playerLocation.getY();
//        Point2D boardPosition = new Point2D(relativeX, relativeY);

        Point2D relativeLocation = getRelativeLocation(stage,player.getLocation());

        // draw map
        gc.save();

        gc.setFill(new ImagePattern(textures.get(Sprites.MAP)));



        // below line is for testing things
        //gc.setFill(Color.WHITE);
        gc.fillRect(relativeLocation.getX(), relativeLocation.getY(), map.getWidth(), map.getHeight());

        // restore previous state
        gc.restore();

    }

    static void drawPowerUp(GraphicsContext gc, Rectangle stage, PowerUp powerUp, Player player) {

        Point2D relativeLocation = getRelativeLocation(stage, powerUp.getLocation(), player.getLocation());

        Color c = colourSwitch.getPowerUpColour(powerUp.getType());
        gc.save();
        gc.setFill(c);

        gc.fillOval(relativeLocation.getX(), relativeLocation.getY(), powerUp.getWidth(), powerUp.getWidth());
        gc.restore();

    }

    static void drawCrosshair(GraphicsContext gc, Rectangle stage) {
        gc.strokeLine(stage.getWidth() / 2, 0, stage.getWidth() / 2, stage.getHeight());
        gc.strokeLine(0, stage.getHeight() / 2, stage.getWidth(), stage.getHeight() / 2);
    }

    static void drawBackground(GraphicsContext gc, Rectangle stageSize, ArrayList<Point2D> stars) {
        gc.save();
        gc.setFill(UIColour.BACKGROUND.getColor());


        gc.setFill(lg1);

        gc.fillRect(0, 0, stageSize.getWidth(), stageSize.getHeight());

        gc.setFill(Color.WHITE);
        for(Point2D star: stars){

            gc.fillOval(star.getX(), star.getY(), 5, 5);
        }

        gc.restore();
    }

    static ArrayList<Point2D> loadStars(Rectangle stageSize) {
        ArrayList<Point2D> stars = new ArrayList<>();
        for (int i = 0; i <= 40; i++) {
            int x = ThreadLocalRandom.current().nextInt(0, (int) (stageSize.getWidth() + 1));
            int y = ThreadLocalRandom.current().nextInt(0, (int) (stageSize.getHeight() + 1));

            stars.add(new Point2D(x, y));

        }
        return stars;
    }

    public static void drawObstacles(GraphicsContext gc, Rectangle stageSize, PhysicsObject obstacle, Player player) {

        Point2D relativeLocation = getRelativeLocation(stageSize, obstacle.getLocation() , player.getLocation());
        //relativeLocation = relativeLocation.add(-obstacle.getWidth() / 2f, -obstacle.getHeight() / 2f);


        gc.save();

        gc.setFill(Color.PURPLE);
        gc.fillRect(relativeLocation.getX(), relativeLocation.getY(), obstacle.getWidth(), obstacle.getHeight());

        gc.restore();
    }

    public static void drawHill(GraphicsContext gc, Rectangle stageSize, Player player, Circle hill) {
        Point2D hillLocation = new Point2D(hill.getCenterX(), hill.getCenterY());
        Point2D relativeLocation = getRelativeLocation(stageSize, hillLocation, player.getLocation());

        // change based upon circle center location
        relativeLocation = relativeLocation.add(- hill.getRadius(), - hill.getRadius());


        gc.save();
        gc.setFill(Color.AQUAMARINE);

        gc.fillOval(relativeLocation.getX(), relativeLocation.getY(), hill.getRadius()*2, hill.getRadius()*2);

        gc.restore();

    }
}
