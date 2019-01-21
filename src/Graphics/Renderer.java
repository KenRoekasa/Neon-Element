package Graphics;

import Debugger.Debugger;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;

import static javafx.scene.transform.Rotate.X_AXIS;


class Renderer {
    private GraphicsContext gc;
    private Debugger debugger;
    private Double scaleConstant;

    Renderer(GraphicsContext gc, Debugger debugger) {
        this.gc = gc;
        this.debugger = debugger;
        // magic number 10/7 * 990/1000
        scaleConstant = (double)99/70;  




    }


    void drawMap(Rectangle stage, Rectangle board, Point2D playerLocation, int playerWidth) {

        Point2D stageCenter = new Point2D(stage.getWidth() / 2, stage.getHeight() / 2);
        Point2D isoPlayerLocation = ISOConverter.twoDToIso(playerLocation);
        isoPlayerLocation = isoPlayerLocation.add(10, 0);


        Point2D boardPosition = new Point2D(stageCenter.getX() - isoPlayerLocation.getX() + playerWidth/2, stageCenter.getY() - isoPlayerLocation.getY() + playerWidth/2);

        Rotate rotateZ = new Rotate(60.0, boardPosition.getX(), boardPosition.getY(), 0, X_AXIS);
        Rotate rotateX= new Rotate(45, boardPosition.getX(), boardPosition.getY());


        debugger.add("player location: " + playerLocation.toString(), 1);
        debugger.add("Player isometric position: " + isoPlayerLocation.toString(), 1);
        debugger.add("Board position: " + boardPosition.toString(), 1);

        // create transformation to rotate by 45 degrees and squash
        gc.save();
        Affine affine = new Affine();
        affine.prepend(rotateX);
        affine.prepend(rotateZ);

        gc.transform(affine);
        // draw map

        gc.strokeRect(boardPosition.getX(), boardPosition.getY(), board.getWidth() * scaleConstant, board.getHeight() * scaleConstant);

        // restore previous state
        gc.restore();

    }

    void drawPlayer(Rectangle stage, Point2D playerLocation, int playerWidth) {

        // Point2D isoLocation = ISOConverter.twoDToIso(playerLocation);
        Point2D stageCenter = new Point2D(stage.getWidth() / 2, stage.getHeight() / 2);

        double playerXCenter = stageCenter.getX();
        double playerYCenter = stageCenter.getY();

        gc.save();
        Affine affine = new Affine();
        affine.appendRotation(45, playerXCenter, playerYCenter);
        affine.prependScale(1, 0.5, new Point2D(playerXCenter, playerYCenter));

        gc.transform(affine);

        gc.setFill(Color.GREEN);
        gc.fillRect(playerXCenter, playerYCenter, playerWidth * scaleConstant, playerWidth * scaleConstant);
        gc.restore();

    }
}
