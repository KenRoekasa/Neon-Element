package graphics.rendering;

import engine.entities.Player;
import engine.entities.PowerUp;
import graphics.enumSwitches.colourSwitch;
import graphics.enums.UIColour;
import graphics.rendering.textures.Sprites;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.paint.*;
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

        double relativeX = stageCenter.getX() - playerLocation.getX() ;
        double relativeY = stageCenter.getY() - playerLocation.getY();
        Point2D boardPosition = new Point2D(relativeX, relativeY);
        // draw map
        gc.save();

        // todo remove string compare
        gc.setFill(new ImagePattern(textures.get(Sprites.MAP)));
        gc.fillRect(boardPosition.getX(), boardPosition.getY(), map.getWidth(), map.getHeight());


        // restore previous state
        gc.restore();

    }

    static void drawPowerUp(GraphicsContext gc, Rectangle stage, PowerUp powerUp, Player player) {

        Point2D relativeLocation = getRelativeLocation(stage, powerUp, player.getLocation());

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
}
