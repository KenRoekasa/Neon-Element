package graphics;

import javafx.animation.PathTransition;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;



public class DrawElements {
    private final static double radius = 22;
    public static void drawSwap(GraphicsContext gc,Circle center,Circle top,Circle left,Circle diagonal){
        gc.save();
        //draw default
        // swap elements in clock-wise based on air => earth => water => fire
        Path path = new Path();
        path.getElements().add (new MoveTo(150f, 70f));
        path.getElements().add (new CubicCurveTo(240f, 230f, 500f, 340f, 600, 50f));

        PathTransition pathTransition = new PathTransition();



        gc.restore();
    }

    public static Circle drawElement(Color colour){
        Circle element =new Circle();
        element.setRadius(radius);
        element.setFill(colour);
        return element;
    }
}
