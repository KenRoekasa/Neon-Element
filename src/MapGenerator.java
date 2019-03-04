import engine.Map;
import javafx.geometry.Point2D;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MapGenerator {

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


    public void isSquare(){

    }

}
