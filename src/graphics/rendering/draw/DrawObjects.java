package graphics.rendering.draw;

import engine.entities.PhysicsObject;
import engine.entities.Player;
import engine.entities.PowerUp;
import graphics.rendering.colourSwitch;
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

/**
 * Contains methods that draw everything that isn't a player or an attack to the screen
 */
public class DrawObjects {

    /**
     *  An Array of the stops needed for the background gradients
     */
    private static Stop[] stops1 = new Stop[]{new Stop(0.2, Color.web("060419")), new Stop(0.4, Color.web("041830")), new Stop(0.7, Color.ORCHID), new Stop(1, Color.ORANGE)};

    /**
     *  The gradient Paint object used for the background
     */
    private static RadialGradient lg1 = new RadialGradient(0, 0, 0.5, 0.5, 0.8, true, CycleMethod.NO_CYCLE, stops1);


    /**
     * Draws the 'map' to the screen
     * @param gc The GraphicsContext being drawn to
     * @param stageSize The size of the stage containing the GraphicsContext
     * @param map   The size of the map
     * @param player    The current client Player
     * @param textures  The HashMap of textures used in the game
     */
    public static void drawMap(GraphicsContext gc, Rectangle stageSize, Rectangle map, Player player, HashMap<Sprites, Image> textures) {

        Point2D relativeLocation = getRelativeLocation(stageSize,player.getLocation());

        // draw map
        gc.save();

        gc.setFill(new ImagePattern(textures.get(Sprites.MAP)));

        gc.fillRect(relativeLocation.getX(), relativeLocation.getY(), map.getWidth(), map.getHeight());

        // restore previous state
        gc.restore();

    }

    /**
     * Draws a PowerUp to the map
     * @param gc The GraphicsContext being drawn to
     * @param stageSize The size of the stage containing the GraphicsContext
     * @param powerUp   The PowerUp being drawn
     * @param player    The current client Player
     */
    public static void drawPowerUp(GraphicsContext gc, Rectangle stageSize, PowerUp powerUp, Player player) {

        Point2D relativeLocation = getRelativeLocation(stageSize, powerUp.getLocation(), player.getLocation());

        Color c = colourSwitch.getPowerUpColour(powerUp.getType());
        gc.save();
        gc.setFill(c);

        gc.fillOval(relativeLocation.getX(), relativeLocation.getY(), powerUp.getWidth(), powerUp.getWidth());
        gc.restore();

    }


    /**
     *  Draws the game background
     * @param gc The GraphicsContext being drawn to
     * @param stageSize The size of the stage containing the GraphicsContext
     * @param stars The ArrayList of 'star' locations
     */
    public static void drawBackground(GraphicsContext gc, Rectangle stageSize, ArrayList<Point2D> stars) {
        gc.save();
        gc.setFill(lg1);

        gc.fillRect(0, 0, stageSize.getWidth(), stageSize.getHeight());

        gc.setFill(Color.WHITE);
        for(Point2D star: stars){

            gc.fillOval(star.getX(), star.getY(), 5, 5);
        }

        gc.restore();
    }

    /**
     * Randomly generates the location of 40 stars
     * @param stageSize     The size of the stage that the stars will be drawn to
     * @return  The ArrayList of star locations
     */
    public static ArrayList<Point2D> loadStars(Rectangle stageSize) {
        ArrayList<Point2D> stars = new ArrayList<>();
        for (int i = 0; i <= 40; i++) {
            int x = ThreadLocalRandom.current().nextInt(0, (int) (stageSize.getWidth() + 1));
            int y = ThreadLocalRandom.current().nextInt(0, (int) (stageSize.getHeight() + 1));

            stars.add(new Point2D(x, y));

        }
        return stars;
    }

    /**
     * Draws any walls to the map
     * @param gc The GraphicsContext being drawn to
     * @param stageSize The size of the stage containing the GraphicsContext
     * @param obstacle  The wall to be drawn
     * @param player    The current client player
     */
    public static void drawWalls(GraphicsContext gc, Rectangle stageSize, PhysicsObject obstacle, Player player) {

        Point2D relativeLocation = getRelativeLocation(stageSize, obstacle.getLocation() , player.getLocation());
        relativeLocation = relativeLocation.add(-obstacle.getWidth() / 2f, -obstacle.getHeight() / 2f);


        gc.save();

        gc.setFill(Color.PURPLE);
        gc.fillRect(relativeLocation.getX(), relativeLocation.getY(), obstacle.getWidth(), obstacle.getHeight());

        gc.restore();
    }

    /**
     * Draws the hill for the king of the hill game mode
     * @param gc The GraphicsContext being drawn to
     * @param stageSize The size of the stage containing the GraphicsContext
     * @param player The current client player
     * @param hill  The size of the hill
     */
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
