package graphics.enumSwitches;

import engine.enums.Elements;
import engine.enums.PowerUpType;
import javafx.scene.paint.Color;

public class colourSwitch {

    public static Color getElementColour(Elements currentElement){
        switch(currentElement){
            case FIRE:
                return Color.web("FE446C");
            case AIR:
                return Color.web("FFAB45");
            case EARTH:
                return Color.web("85FD44");
            case WATER:
                return Color.web("4DB8F8");
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
