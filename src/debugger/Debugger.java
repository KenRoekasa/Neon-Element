package debugger;

import javafx.scene.canvas.GraphicsContext;

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

        int prevHeight = 20;

        for(int i = 0; i < items; i++){

            Pair values = output.remove(0);
            String message = (String)values.getLeft();

            gc.strokeText(message, 20, prevHeight);

            // calculates height of next message based upon number of previous lines
            int numLines = (int)values.getRight();
            prevHeight = prevHeight + 20 * numLines;

        }
    }

    // adds message to be printed - requires the message and the number of lines (number of newline characters)
    public void add(String toPrint, int numLines) {
        output.add(new Pair<>(toPrint, numLines));
    }

}
