package Graphics;

import Debugger.Debugger;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Affine;


class Renderer {
    private GraphicsContext gc;
    private Debugger debugger;
    
     Renderer(GraphicsContext gc, Debugger debugger){
        this.gc = gc;
        this.debugger = debugger;
    }


     void drawMap(Point2D mapPosition, Point2D playerLocation, Rectangle board) {

        // create transformation to rotate by 45 degrees and squash
        gc.save();
        Affine affine = new Affine();
        //affine.appendRotation(45, mapXIso,mapYIso);
        affine.prependScale(1,0.5);

        //gc.transform(affine);
        // draw map
        gc.strokeRect(mapPosition.getX(), mapPosition.getY(), board.getWidth(), board.getHeight());

        // restore previous state
        gc.restore();

        //print grid location

        String debug = "X coord: " + mapPosition.getX() + "\nY coord: " + mapPosition.getY()
                + "\nX coord center relative: " + playerLocation.getX() + " Y coord center relative: " + playerLocation.getY();


        debugger.add(debug, 3);

    }

    void drawPlayer(double playerX, double playerY, int playerWidth) {

        gc.setFill(Color.GREEN);
        gc.fillRect(playerX - playerWidth/2, playerY - playerWidth/ 2, playerWidth,playerWidth);

    }
}
