package enumSwitches;

import enums.Action;
import enums.Sound;

public class soundSwitch {
    public static Sound switchSound(Action action){
        switch (action){
            case LIGHT:
                return Sound.LIGHT_ATTACK;
            case HEAVY:
                return Sound.HEAVY_ATTACK;
            case BLOCK:
                return Sound.SHIELD;
            case CHARGE:
                return Sound.CHARGE;
        }
        return null;
    }
}
