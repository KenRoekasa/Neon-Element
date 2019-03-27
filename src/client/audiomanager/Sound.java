package client.audiomanager;

import engine.model.enums.Action;

/**
 *  The available spot effects
 *  <li>{@link #LIGHT_ATTACK}</li>
 *  <li>{@link #HEAVY_ATTACK}</li>
 *  <li>{@link #CHARGE}</li>
 *  <li>{@link #LIGHT_HIT}</li>
 *  <li>{@link #HEAVY_HIT}</li>
 *  <li>{@link #SHIELD}</li>
 *  <li>{@link #BUTTON1}</li>
 *  <li>{@link #BUTTON2}</li>
 *  <li>{@link #BUTTON3}</li>
 *  <li>{@link #BUTTON4}</li>
 *  <li>{@link #HOVER1}</li>
 *  <li>{@link #HOVER2}</li>
 */
public enum Sound {

    /**
     *  Light attack sound
     */
    LIGHT_ATTACK("audio/light_attack.mp3"),

    /**
     *  Heavy attack sound
     */
    HEAVY_ATTACK("audio/heavy_attack.mp3"),

    /**
     *  Attack charge sound
     */
    CHARGE("audio/charge.mp3"),

    /**
     *  Light attack successful hit sound
     */
    LIGHT_HIT("audio/light_hit.mp3"),

    /**
     *  Heavy attack successful hit sound
     */
    HEAVY_HIT("audio/heavy_hit.mp3"),

    /**
     *  Shield sound
     */
    SHIELD("audio/shield.wav"),

    /**
     *  First button sound
     */
    BUTTON1("audio/button_1.mp3"),

    /**
     *  Second button sound
     */
    BUTTON2("audio/button_2.mp3"),

    /**
     *  Third button sound
     */
    BUTTON3("audio/button_3.mp3"),

    /**
     *  Fourth button sound
     */
    BUTTON4("audio/button_4.mp3"),

    /**
     *  First hover sound
     */
    HOVER1("audio/hover_1.mp3"),

    /**
     *  Second hover sound
     */
    HOVER2("audio/hover_2.mp3");

    /**
     *  The path of the effects mp3 file
     */
    private final String path;

    /**
     *  Sound enum constructor
     * @param location The location of the effects mp3 file
     */
    Sound(String location) {
        path = location;
    }

    /**
     * Gets the path to the mp3 file
     * @return The path to the mp3 file
     */
    public String getPath() {
        return path;
    }

    /**
     *  Gets the sound value for a provided action
     * @param action The action provided
     * @return  The sound for provided action
     */
    // todo expand
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
                default:
                    return Sound.BUTTON1;
        }
    }
}
