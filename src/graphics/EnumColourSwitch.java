package graphics;

import enums.Elements;
import enums.PowerUpType;
import javafx.scene.paint.Color;

public class EnumColourSwitch {

    public static Color getElementColour(Elements currentElement){
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

    public static Color getPowerUpColour(PowerUpType powerUp){
        switch(powerUp){
            case SPEED:
                return Color.ORANGE;
            case HEAL:
                return Color.PURPLE;
            case DAMAGE:
                return Color.FUCHSIA;

            default:
                return Color.BLACK;
        }
    }

}
