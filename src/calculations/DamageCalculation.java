package calculations;

import entities.Player;
import enums.Action;

public class DamageCalculation {

    /**
     * Returns the amount of damage dealt to the victim
     *
     * @return damage to be dealt to the victim
     */
    public static float calculateDealtDamage(Player player, entities.Character victim) {
        // Deal damage base on what kind of attack it is
        if (player.getCurrentAction() == Action.LIGHT) {
            return calculateDamage(3, player, victim) * calculateMitgation(player, victim) * player.getDamageMultiplier();
        }
        if (player.getCurrentAction() == Action.HEAVY) {
            return calculateDamage(10, player, victim) * calculateMitgation(player, victim) * player.getDamageMultiplier();
        }
        return 0;
    }


    private static float calculateDamage(float baseDmg, Player player, entities.Character victim) {
        switch (player.getCurrentElement()) {
            case FIRE:
                switch (victim.getCurrentElement()) {
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
                switch (victim.getCurrentElement()) {
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
                switch (victim.getCurrentElement()) {
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
                switch (victim.getCurrentElement()) {
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
    private static float calculateMitgation(Player attackingPlayer, entities.Character victim) {
        if (victim.isShielded()) {
            switch (attackingPlayer.getCurrentElement()) {
                case FIRE:
                    switch (victim.getCurrentElement()) {
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
                    switch (victim.getCurrentElement()) {
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
                    switch (victim.getCurrentElement()) {
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
                    switch (victim.getCurrentElement()) {
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
