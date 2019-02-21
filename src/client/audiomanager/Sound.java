package client.audiomanager;

public enum Sound {
    LIGHT_ATTACK("audio/light_attack.mp3"),
    HEAVY_ATTACK("audio/heavy_attack.mp3"),
    CHARGE("audio/charge_attack.mp3"),
    LIGHT_HIT("audio/light_hit.mp3"),
    HEAVY_HIT("audio/heavy_hit.mp3"),
    SHIELD("audio/shield.mp3");

    private final String path;

    Sound(String s) {
        path = s;
    }

    public String getPath() {
        return path;
    }


}
