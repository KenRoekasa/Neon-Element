package graphics.rendering.draw;

import javafx.animation.PathTransition;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

public class DrawElements {
    private final static double RADIUS = 22;
    // relative location based
    private final static double CENTER_X = 0;
    private final static double CENTER_Y = 0;
    private final static double LEFT_X = 0;
    private final static double LEFT_Y= 0;
    private final static double DIAGONAL_X =0;
    private final static double DIAGONAL_Y =0;
    private final static double TOP_X =0;
    private final static double TOP_Y =0;

    public static void drawSwap(GraphicsContext gc,Circle center,Circle top,Circle left,Circle diagonal){
        gc.save();
        //draw default
        // swap elements in clock-wise based on air => earth => water => fire
        Path c2l = new Path();//center to left
        Path l2d = new Path();
        Path d2t = new Path();
        Path t2c = new Path();

        PathTransition c2l_tst = new PathTransition();
        PathTransition l2d_tst =new PathTransition();
        PathTransition d2t_tst=new PathTransition();
        PathTransition t2c_tst =new PathTransition();

        LineTo c2l_line =new LineTo(LEFT_X,LEFT_Y);
        LineTo l2d_line = new LineTo(DIAGONAL_X,DIAGONAL_Y);
        LineTo d2t_line =new LineTo(TOP_X,TOP_Y);
        LineTo t2c_line = new LineTo(CENTER_X,CENTER_Y);

        c2l.getElements().add(c2l_line);
        l2d.getElements().add(l2d_line);
        d2t.getElements().add(d2t_line);
        t2c.getElements().add(t2c_line);

        c2l_tst.setPath(c2l);
        l2d_tst.setPath(l2d);
        d2t_tst.setPath(d2t);
        t2c_tst.setPath(t2c);

        PathTransition pathTransition = new PathTransition();

        gc.restore();
    }

    public static Circle drawElement(Color colour){
        Circle element =new Circle();
        element.setRadius(RADIUS);
        element.setFill(colour);
        return element;
    }
}
