package engine;

import engine.Map;
import engine.entities.Wall;
import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MapGenerator {


    /**
     * @return the default map of the game
     */
    public static Map createMap1() {

        ArrayList<Wall> walls = new ArrayList<>();
        walls.add(new Wall(100, 100, 700, 100));
        walls.add(new Wall(100, 200, 100, 600));
        walls.add(new Wall(100, 1200, 100, 600));
        walls.add(new Wall(100, 1900, 700, 100));

        walls.add(new Wall(1200, 100, 700, 100));
        walls.add(new Wall(1800, 200, 100, 600));
        walls.add(new Wall(1800, 1200, 100, 600));
        walls.add(new Wall(1200, 1900, 600, 100));

        walls.add(new Wall(400, 400, 300, 300));

        walls.add(new Wall(400, 1200, 300, 300));

        walls.add(new Wall(1300, 400, 300, 300));

        walls.add(new Wall(800, 800, 400, 400));

        return new Map(new Rectangle(2000, 2000), walls);
    }


    @Deprecated
    public Map mapFromImage() throws IOException {
        File file = new File("your_file.jpg");
        BufferedImage image = ImageIO.read(file);
        int width = image.getWidth();
        int height = image.getHeight();
        // Getting pixel color by position x and y
        int index = 0;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int clr = image.getRGB(i, j);
                int red = (clr & 0x00ff0000) >> 16;
                int green = (clr & 0x0000ff00) >> 8;
                int blue = clr & 0x000000ff;
                Color color = new Color(red, green, blue);
                // if pixel is black add the location of the point to the point array
                Point2D[] pointArr = new Point2D[width * height];
                if (color.equals(Color.BLACK)) {
                    pointArr[index++] = new Point2D(i, j);
                }

            }
        }


        return null;
    }


}
