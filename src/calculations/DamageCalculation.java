package calculations;

import entities.Player;

public class DamageCalculation {


    public float calculateDamage(float baseDmg, Player player, Player victim) {
        switch (player.getState()) {
            case FIRE:
                switch (victim.getState()) {
                    case FIRE:
                        return baseDmg * 1f;
                    case EARTH:
                        return baseDmg * 1f;
                    case WATER:
                        return baseDmg * 0.5f;
                    case AIR:
                        return baseDmg * 1.5f;
                }
            case EARTH:
                switch (victim.getState()) {
                    case FIRE:
                        return baseDmg * 1.5f;
                    case EARTH:
                        return baseDmg * 1f;
                    case WATER:
                        return baseDmg * 0.5f;
                    case AIR:
                        return baseDmg * 1f;
                }
            case WATER:
                switch (victim.getState()) {
                    case FIRE:
                        return baseDmg * 1.5f;
                    case EARTH:
                        return baseDmg * 1f;
                    case WATER:
                        return baseDmg * 1f;
                    case AIR:
                        return baseDmg * 0.5f;
                }
            case AIR:
                switch (victim.getState()) {
                    case FIRE:
                        return baseDmg * 1.5f;
                    case EARTH:
                        return baseDmg * 0.5f;
                    case WATER:
                        return baseDmg * 1f;
                    case AIR:
                        return baseDmg * 1f;
                }
            default:
                return baseDmg;

        }
    }

    //The amount the damaged is reduced by, due to the shields in percentage
    public float calculateMitgation(Player attackingPlayer, Player victim) {
        if (victim.getIsShielded()) {
            switch (attackingPlayer.getState()) {
                case FIRE:
                    switch (victim.getState()) {
                        case FIRE:
                            return 0.5f;
                        case EARTH:
                            return 1.0f;
                        case WATER:
                            return 1.0f;
                        case AIR:
                            return 0.75f;
                    }
                case EARTH:
                    switch (victim.getState()) {
                        case FIRE:
                            return 0.75f;
                        case EARTH:
                            return 0.5f;
                        case WATER:
                            return 0.95f;
                        case AIR:
                            return 0.95f;
                    }
                case WATER:
                    switch (victim.getState()) {
                        case FIRE:
                            return 0.5f;
                        case EARTH:
                            return 0.25f;
                        case WATER:
                            return 0.5f;
                        case AIR:
                            return 1f;
                    }
                case AIR:
                    switch (victim.getState()) {
                        case FIRE:
                            return 1.0f;
                        case EARTH:
                            return 0.75f;
                        case WATER:
                            return 0.75f;
                        case AIR:
                            return 0.5f;
                    }
                default:
                    return 1.0f;
            }
        } else {
            return
                    1.0f;
        }

    }


}
