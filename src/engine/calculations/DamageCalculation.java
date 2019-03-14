package engine.calculations;

import engine.entities.Player;
import engine.enums.Action;
import engine.enums.ObjectType;

/**
 * The calculator for the amount of damage a player inflicts on another player
 */
public class DamageCalculation {

    /**
     * Returns the amount of damage dealt to the victim
     *
     * @return damage to be dealt to the victim
     */
    public static float calculateDealtDamage(Player player, engine.entities.Character victim) {
        // Deal damage base on what kind of attack it is
        if (player.getCurrentAction() == Action.LIGHT) {
            if (victim.getTag() == ObjectType.ENEMY) {
                return calculateDamage(3, player, victim) * calculateMitgation(player, victim) * player.getDamageMultiplier() * 5;
            }
            return calculateDamage(3, player, victim) * calculateMitgation(player, victim) * player.getDamageMultiplier();
        }
        if (player.getCurrentAction() == Action.HEAVY) {
            if (victim.getTag() == ObjectType.ENEMY) {
                return calculateDamage(20, player, victim) * calculateMitgation(player, victim) * player.getDamageMultiplier() * 4;
            }
            return calculateDamage(20, player, victim) * calculateMitgation(player, victim) * player.getDamageMultiplier();
        }
        return 0;
    }


    private static float calculateDamage(float baseDmg, Player player, engine.entities.Character victim) {
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

    /**
     * The amount the damaged is reduced by, due to the shields in percentage
     *
     * @param attackingPlayer the player attacking
     * @param victim          the player being attack/taking damage
     * @return the reduction of incoming damage in percentage
     */

    private static float calculateMitgation(Player attackingPlayer, engine.entities.Character victim) {
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
