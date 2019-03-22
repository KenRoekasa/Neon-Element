package client.audiomanager;

/**
 *  The available music / audio tracks
 *  <li>{@link #MENU}</li>
 *  <li>{@link #GAME}</li>
 *  <li>{@link #NEON}</li>
 */

public enum Music {

    /**
     *  Menu screen music
     */
	MENU("audio/menu_theme.mp3"),

    /**
     *  Game screen music
     */
    GAME("audio/game_theme.mp3"),

    /**
     *  Menu screen neon hum effect
     */
    NEON("audio/neon_flicker.mp3");

    /**
     * The path of the musics mp3 file
     */
    private final String path;

    /**
     * Music enum constructor
     * @param location The location of the mp3 file
     */
    Music(String location) {
        path = location;
    }


    /**
     * Gets the path to the musics mp3
     * @return The path of the musics mp3
     */
    public String getPath() {
        return path;
    }

}
