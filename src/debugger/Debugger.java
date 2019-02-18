package debugger;

import client.ClientGameState;
import entities.Character;
import entities.Enemy;
import entities.PhysicsObject;
import graphics.ISOConverter;
import graphics.Renderer;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

/*
* When created, this object allows for debug information to easily be added to the screen
* */

public class Debugger {
    private GraphicsContext gc;
    private ArrayList<Pair<String, Integer>> output;


    public Debugger(GraphicsContext gc){
        this.gc = gc;
        output = new  ArrayList<Pair<String, Integer>>();
    }

    // prints all items added to debugger to screen
    public void print(){
        int items = output.size();

        int prevHeight = 100;
        for(int i = 0; i < items; i++){

            Pair values = output.remove(0);
            String message = (String)values.getLeft();

            gc.save();
            gc.setStroke(Color.WHITE);
            gc.strokeText(message, 20, prevHeight);

            // calculates height of next message based upon number of previous lines
            int numLines = (int)values.getRight();
            prevHeight = prevHeight + 20 * numLines;

            gc.restore();
        }
    }

    public void simpleGSDebugger(ClientGameState gameState, Debugger debugger) {
        debugger.add(gameState.getPlayer().toString(), 4);

        for (Enemy enemy: gameState.getEnemies()){
            debugger.add(enemy.toString(), 4);
        }


    }


    public void gameStateDebugger(ClientGameState gameState, Rectangle stage){
        Point2D stageCenter = new Point2D(stage.getWidth()/2, stage.getHeight()/2 + 20);
        printPlayerInfo(gameState.getPlayer(), stageCenter);

        for (Enemy enemy: gameState.getEnemies()){

            Point2D relativeLocation = Renderer.getRelativeLocation(stage, enemy, gameState.getPlayer().getLocation());

            //relativeLocation = ISOConverter.twoDToIso(relativeLocation);
            relativeLocation.add(0, 30);
            printPlayerInfo(enemy, relativeLocation );

        }
    }

    private void printPlayerInfo(Character player, Point2D relativeLocation){

        gc.strokeText(player.toString(), relativeLocation.getX(), relativeLocation.getY());
    }

    // adds message to be printed - requires the message and the number of lines (number of newline characters)
    public void add(String toPrint, int numLines) {
        output.add(new Pair<>(toPrint, numLines));
    }

}
