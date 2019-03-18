package client.audiomanager;

import engine.enums.Action;

public enum Sound {
    LIGHT_ATTACK("audio/light_attack.mp3"),
    HEAVY_ATTACK("audio/heavy_attack.mp3"),
    CHARGE("audio/charge.mp3"),
    LIGHT_HIT("audio/light_hit.mp3"),
    HEAVY_HIT("audio/heavy_hit.mp3"),
    SHIELD("audio/shield.mp3"),
    BUTTON1("audio/button_1.mp3"),
    BUTTON2("audio/button_2.mp3"),
    BUTTON3("audio/button_3.mp3"),
    BUTTON4("audio/button_4.mp3"),
    HOVER1("audio/hover_1.mp3"),
    HOVER2("audio/hover_2.mp3");

    private final String path;

    Sound(String s) {
        path = s;
    }

    public String getPath() {
        return path;
    }


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
        }
        return null;
    }


}
