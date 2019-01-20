package Debugger;

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

    public void print(){
        int items = output.size();

        int prevHeight = 0;

        for(int i = 0; i < items; i++){

            Pair values = output.remove(0);

            String message = (String)values.getLeft();
            int numLines = (int)values.getRight();

            int height = prevHeight + 15 * numLines;

            gc.strokeText(message, 20, height);

            prevHeight = height;

        }
    }


    public void add(String toPrint, int numLines) {
        output.add(new Pair<>(toPrint, numLines));
    }

}
