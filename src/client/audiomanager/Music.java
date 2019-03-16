package client.audiomanager;

public enum Music {
	MENU("audio/menu_theme.mp3"),
    GAME("audio/game_theme.mp3");

    private final String path;

    Music(String s) {
        path = s;
    }

    public String getPath() {
        return path;
    }

}
