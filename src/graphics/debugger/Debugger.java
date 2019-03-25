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


/**
 * When created, this object allows for debug information to easily be added to the screen
 */
public class Debugger {

    /**
     *  The GraphicsContext which will be drawn to
     */
    private GraphicsContext gc;

    /**
     * An ArrayList of the Strings to be printed, along with the number of lines they take up
     */
    private ArrayList<Pair<String, Integer>> output;


    /**
     * The constructor for the debugger
     * @param graphicsContext The GraphicsContext which the debugger will draw to
     */
    public Debugger(GraphicsContext graphicsContext){
        this.gc = graphicsContext;
        output = new ArrayList<>();
    }

    /**
     *  Prints all items added to the ArrayList to the screen
     */
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

    /**
     * Prints the toString of each player to the corner of the screen
     * @param gameState The current GameState
     * @param debugger  The debugger object to print to
     * @deprecated
     */
    public void simpleGSDebugger(ClientGameState gameState, Debugger debugger) {
        debugger.add(gameState.getPlayer().toString(), 4);

        for (Player enemy: gameState.getOtherPlayers(gameState.getPlayer())){
            debugger.add(enemy.toString(), 4);
        }
    }


    /**
     *  Prints the health of each player above their sprite
     *  Calls the printPlayerHealth method for each player
     * @param gameState The current GameState
     * @param stage The size of the stage in which the game is taking place
     */
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


    /**
     * Prints the toString of a given player to a position near their sprite
     * @param player The player whose information is to be printed
     * @param relativeLocation  The location of the players sprite on the screen
     * @deprecated
     */
    private void printPlayerInfo(Character player, Point2D relativeLocation){
        gc.save();

        gc.setStroke(Color.WHITE);
        gc.strokeText(player.toString(), relativeLocation.getX(), relativeLocation.getY());
        gc.restore();
    }

    /**
     * Prints the health of a given player to a position near their sprite
     * @param player The player whose health is to be printed
     * @param relativeLocation The location of the players sprite on the screen
     */
    private void printPlayerHealth(Character player, Point2D relativeLocation){

        gc.strokeText(String.valueOf((int)player.getHealth()), relativeLocation.getX(), relativeLocation.getY());
    }


    /**
     * Adds a message to be printed
     * @param toPrint The message to be printed
     * @param numLines The number of lines the message prints to
     */
    public void add(String toPrint, int numLines) {
        output.add(new Pair<>(toPrint, numLines));
    }

}
