package graphics.debugger;

import client.ClientGameState;
import engine.entities.Character;

import engine.entities.Player;
import graphics.rendering.ISOConverter;
import graphics.rendering.Renderer;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

import static graphics.rendering.ISOConverter.getPlayerLocOnScreen;

/*
* When created, this object allows for debug information to easily be added to the screen
* */

public class Debugger {
    private GraphicsContext gc;
    private ArrayList<Pair<String, Integer>> output;


    public Debugger(GraphicsContext gc){
        this.gc = gc;
        output = new ArrayList<>();
    }

    // prints all items added to graphics.debugger to screen
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

        for (Player enemy: gameState.getOtherPlayers(gameState.getPlayer())){
            debugger.add(enemy.toString(), 4);
        }

    }


    public void gameStateDebugger(ClientGameState gameState, Rectangle stage){
        Point2D playerLocationOnScreen = getPlayerLocOnScreen(gameState.getPlayer(), stage);
        printPlayerHealth(gameState.getPlayer(), playerLocationOnScreen);

        for (Player enemy: gameState.getOtherPlayers(gameState.getPlayer())){

            Point2D relativeLocation = Renderer.getRelativeLocation(stage, enemy.getLocation(), gameState.getPlayer().getLocation());

            Point2D newLoc = ISOConverter.getLocationOnScreen(relativeLocation, enemy, stage);
            newLoc = newLoc.add(-15, 0);

            printPlayerHealth(enemy, newLoc);

        }
    }

    private void printPlayerInfo(Character player, Point2D relativeLocation){

        gc.strokeText(player.toString(), relativeLocation.getX(), relativeLocation.getY());
    }

    private void printPlayerHealth(Character player, Point2D relativeLocation){

        gc.strokeText(String.valueOf((int)player.getHealth()), relativeLocation.getX(), relativeLocation.getY());
    }


    // adds message to be printed - requires the message and the number of lines (number of newline characters)
    public void add(String toPrint, int numLines) {
        output.add(new Pair<>(toPrint, numLines));
    }

}
