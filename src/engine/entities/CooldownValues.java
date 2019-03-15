package engine.entities;

/**
 * Contains the id of the last time that certain cooldown was activated, the duration and the cooldown
 */
public class CooldownValues {

    static int speedBoostID = 0;
    static int speedBoostDuration = 4;
    static int lightAttackID = 1;
    static float lightAttackCD = 0.30f; //attack speed
    static int heavyAttackID = 2;
    static float heavyAttackCD = 7.5f;
    static int changeStateID = 3;
    static float changeStateCD = 1.5f;
    static int damageBoostID = 4;
    static float damageBoostDur = 5;
}
