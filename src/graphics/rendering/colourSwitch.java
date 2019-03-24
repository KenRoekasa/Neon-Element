package graphics.rendering;

import engine.model.enums.Elements;
import engine.model.enums.PowerUpType;
import javafx.scene.paint.Color;

/**
 * Contains methods for switching enums with relative colours
 */
public class colourSwitch {

    /**
     * Returns the color of a given element state
     * @param currentElement The element
     * @return  The color
     */
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


    /**
     * Returns the color of a given PowerUp type
     * @param powerUp The PowerUp
     * @return  The color
     */
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
