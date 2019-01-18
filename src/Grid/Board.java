package Grid;


import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

import javax.swing.*;


public class Board extends Application {

    private int boardWidth;
    private int boardHeight;


    public static int [] [] map = {{1,0,1,0,0},
                                    {1,0,1,0,0},
                                    {1,0,1,0,0},
                                    {0,0,0,0,0},
                                    {0,0,0,0,1}};


    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Game");


        Group root = new Group();
        Scene theScene = new Scene( root );
        primaryStage.setScene( theScene );

        Canvas canvas = new Canvas( 800, 800);
        boardWidth = 800;
        boardHeight = 800;

        root.getChildren().add( canvas );

        GraphicsContext gc = canvas.getGraphicsContext2D();


        int y_num = map.length;

        for(int y = 0; y < y_num; y++) {
            // get number of x values
            int x_num = map[y].length;
            for(int x = 0; x < x_num; x++) {


                int type = map[y][x];


                int x_pos = (x * 40) ;
                int y_pos = (y * 40) ;

                Point2D location = new Point2D(x_pos, y_pos);



                drawTile(gc, type, location);
            }
        }

        primaryStage.show();
    }

    private void drawTile(GraphicsContext gc, int type, Point2D coords) {

        String image_name = TileTypes.getType(type).toString();

        String location = "Grid/" + image_name + ".png";

        Image i = new Image(location);

        // rotate square
        gc.save();

        // create transformation to rotate by 45 degrees and squash
        Affine affine = new Affine();
        affine.appendRotation(45);
        affine.prependScale(1,0.5);

        gc.transform(affine);

        gc.drawImage(i, coords.getX() + 400, coords.getY() + 200);

        gc.restore();
    }

}
