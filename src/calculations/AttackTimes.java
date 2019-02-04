package calculations;

import enums.Action;

public class AttackTimes {
    public static long getActionTime(Action a) {
        switch (a) {
            case HEAVY:
                return 5000;
            case LIGHT:
                return 100;
            default:
                // todo
                return 5;
        }
    }

}
