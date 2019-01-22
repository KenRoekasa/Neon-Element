package Calculations;

import Entities.Player;

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
                switch (victim.getState()){
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
                switch (victim.getState()){
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
                switch (victim.getState()){
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


}
