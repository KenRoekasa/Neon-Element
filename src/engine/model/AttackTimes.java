package engine.model;

import engine.model.enums.Action;

/**
 * Store the times for how long an attack(animation) last
 */
public class AttackTimes {
    public static long getActionTime(Action a) {
        switch (a) {
            case HEAVY:
                return 200;
            case LIGHT:
                return 100;
            case CHARGE:
                return 2500;
            default:
                // todo
                return 5;
        }
    }

}
