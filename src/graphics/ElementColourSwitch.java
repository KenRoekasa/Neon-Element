package graphics;

import enums.Elements;
import javafx.scene.paint.Color;

public class ElementColourSwitch {

    public static Color getColour(Elements currentElement){
        switch(currentElement){
            case FIRE:
                return Color.RED;
            case AIR:
                return Color.LIGHTSTEELBLUE;
            case EARTH:
                return Color.GREEN;
            case WATER:
                return Color.BLUE;
            default:
                return Color.BLACK;
        }

    }

}
